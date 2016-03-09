package org.culturegraph.mf.streamstyle;

import static java.util.Arrays.asList;
import static org.culturegraph.mf.streamstyle.InlineModules.flatMap;
import static org.culturegraph.mf.streamstyle.InlineModules.map;
import static org.culturegraph.mf.streamstyle.Module.module;

import java.util.List;
import java.util.function.Predicate;

import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.stream.converter.FormetaDecoder;
import org.culturegraph.mf.stream.converter.FormetaEncoder;
import org.culturegraph.mf.stream.converter.LineReader;
import org.culturegraph.mf.stream.pipe.LineSplitter;
import org.culturegraph.mf.stream.pipe.ObjectLogger;
import org.culturegraph.mf.stream.pipe.StreamLogger;
import org.culturegraph.mf.stream.source.StringReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] argv) {
		LOG.info("-------------------- Simple Single Branch Flow --------------------- ");
		simpleSingleBranchFlow();

		LOG.info("-------------- Flow with Duplicating Objects Branches -------------- ");
		flowWithDuplicatingObjectsBranches();

		LOG.info("-------------- Flow with Duplicating Stream Branches --------------- ");
		flowWithDuplicatingStreamBranches();

		LOG.info("----------- Flow Using Metamorph to Dispatch to Branches ----------- ");
		flowUsingMetamorphToDispatchToBranches();

		LOG.info("------------------- Flow with Lambda Expressions ------------------- ");
		flowWithLambdaExpressions();

		LOG.info("------------------------- Terminating Flow ------------------------- ");
		terminatingFlow();

		LOG.info("------------------ Single Module Terminating Flow ------------------ ");
		singleModuleTerminatingFlow();

		LOG.info("------------------ Flow with Terminating Branches ------------------ ");
		flowWithTerminatingBranches();
	}

	private static void simpleSingleBranchFlow() {
		final Flow<ObjectReceiver<String>, ObjectReceiver<String>> flow =
				Flow.startWith(module(new StringReader()))
						.followedBy(module(new LineReader()))
						.followedBy(module(new FormetaDecoder()))
						.followedBy(module(new FormetaEncoder()));

		final List<String> records =
				Flux.process("R1 { lit1: val1}\nR2 {lit2: val2}")
						.toObjectsWith(flow)
						.collectResults();

		records.forEach(System.out::println);
	}

	private static void flowWithDuplicatingObjectsBranches() {
		final Flow<ObjectReceiver<String>, ObjectReceiver<String>> flow =
				Flow.startWith(module(new LineSplitter()))
						.dispatchWith(new DuplicateObjectsStrategy<>())
								.to(module(new ObjectLogger<>("ObjectLogger: ")))
								.to(
										Flow.startWith(module(new FormetaDecoder()))
												.followedBy(module(new StreamLogger("StreamLogger: ")))
												.followedBy(module(new FormetaEncoder())))
								.join(new NaiveObjectsJoiner<>());

		Flux.process("R1 { lit1: val1}\nR2 {lit2: val2}")
				.toObjectsWith(flow)
				.discardResults();
	}

	private static void flowWithDuplicatingStreamBranches() {
		final Flow<ObjectReceiver<String>, ObjectReceiver<String>> flow =
				Flow.startWith(module(new LineSplitter()))
						.followedBy(module(new FormetaDecoder()))
						.dispatchWith(new DuplicateStreamStrategy())
								.to(module(new StreamLogger("Logger 1: ")))
								.to(
										Flow.startWith(module(new StreamLogger("Logger 2: ")))
												.followedBy(module(new StreamLogger("Logger 3: "))))
								.join(new NaiveStreamJoiner())
						.followedBy(module(new FormetaEncoder()));

		Flux.process("R1 { lit1: val1}\nR2 {lit2: val2}")
				.toObjectsWith(flow)
				.discardResults();
	}

	private static void flowUsingMetamorphToDispatchToBranches() {
		final Flow<ObjectReceiver<String>, ObjectReceiver<String>> mmFlow =
				Flow.startWith(module(new LineSplitter()))
						.followedBy(module(new FormetaDecoder()))
						.dispatchWith(new MetamorphStrategy("filter.xml"))
						.to(Predicate.isEqual("stream-me"), module(new StreamLogger("StreamLogger: ")))
						.to(Predicate.isEqual("object-me"),
								Flow.startWith(module(new FormetaEncoder()))
										.followedBy(module(new ObjectLogger<>("ObjectLogger: ")))
										.followedBy(module(new FormetaDecoder())))
						.join(new NaiveStreamJoiner())
						.followedBy(module(new FormetaEncoder()));

		Flux.process("1{decider: object-me, lit1: val1}\n2{decider: stream-me, lit2: val2}\n2{decider: ignore-me, lit3: val3}")
				.toObjectsWith(mmFlow)
				.collectResults()
				.stream()
				.forEach(System.out::println);
	}

	private static void flowWithLambdaExpressions() {
		final Flow<ObjectReceiver<String>, ObjectReceiver<String>> flow =
				Flow.startWith(flatMap((String obj) -> asList(obj.split(" "))))
						.followedBy(map(obj -> "id { literal: '" + obj + "' }"))
						.followedBy(module(new FormetaDecoder()))
						.followedBy(module(new StreamLogger()))
						.followedBy(module(new FormetaEncoder()))
						.followedBy(map(obj -> "{" + obj + "}"));

		Flux.process("A B C D")
				.toObjectsWith(flow)
				.collectResults()
				.stream()
				.forEach(System.out::println);
	}

	private static void terminatingFlow() {
		final TerminatedFlow<ObjectReceiver<String>, ObjectReceiver<String>> flow =
				Flow.startWith(flatMap((String obj) -> asList(obj.split(" "))))
					.followedBy(TerminatingModule.module(new ObjectLogger<>()));

		Flux.process("A B C D")
				.with(flow);
	}

	private static void singleModuleTerminatingFlow() {
		final TerminatedFlow<ObjectReceiver<String>, ObjectReceiver<String>> flow =
				Flow.startWith(TerminatingModule.module(new ObjectLogger<>()));

		Flux.process("A B C D")
				.with(flow);
	}

	private static void flowWithTerminatingBranches() {
		final TerminatedFlow<ObjectReceiver<String>, ?> flow =
				Flow.startWith(flatMap((String obj) -> asList(obj.split(" "))))
						.dispatchWith(new DuplicateObjectsStrategy<>())
								.to(TerminatingModule.module(new ObjectLogger<>()))
								.to(
										Flow.startWith(map((String obj) -> "(" + obj + ")"))
												.followedBy(TerminatingModule.module(new ObjectLogger<>())))
								.terminate();

		Flux.process("A B C D")
				.with(flow);
	}

}
