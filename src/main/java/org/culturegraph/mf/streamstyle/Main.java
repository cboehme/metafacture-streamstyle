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
import org.culturegraph.mf.stream.pipe.LineSplitter;
import org.culturegraph.mf.stream.pipe.ObjectLogger;
import org.culturegraph.mf.stream.pipe.StreamLogger;

public class Main {

	public static void main(final String[] argv) {

		final Flow<ObjectReceiver<String>, ObjectReceiver<String>> mmFlow =
				Flow.startWith(module(new LineSplitter()))
						.followedBy(module(new FormetaDecoder()))
						.dispatchWith(new MetamorphStrategy("filter.xml"))
								.to(Predicate.isEqual("1"), module(new StreamLogger("L1: ")))
								.to(Predicate.isEqual("2"),
										Flow.startWith(module(new FormetaEncoder()))
												.followedBy(module(new ObjectLogger<>("L2: ")))
												.followedBy(module(new FormetaDecoder())))
						.join(new NaiveStreamJoiner())
						.followedBy(module(new FormetaEncoder()));

		Flux.process("1{decider: 1, lit1: val1}\n2{decider: 2, lit2: val2}\n2{decider: 3, lit3: val3}")
				.toObjectsWith(mmFlow)
				.collectResults()
				.stream()
				.forEach(System.out::println);

		final Flow<ObjectReceiver<String>, ObjectReceiver<String>> myFlow =
				Flow.startWith(flatMap((String obj) -> asList(obj.split("\n"))))
						.followedBy(module(new FormetaDecoder()))
						.followedBy(module(new StreamLogger("Logger A: ")))
						.followedBy(module(new FormetaEncoder()));

		final Flow<ObjectReceiver<String>, ObjectReceiver<String>> myFlow2 =
				Flow.startWith(module(new FormetaDecoder()))
						.dispatchWith(new DuplicateStreamStrategy())
								.to(Flow
										.startWith(module(new FormetaEncoder()))
										.followedBy(module(new ObjectLogger<>("Logger X: ")))
										.followedBy(module(new FormetaDecoder()))
										.followedBy(module(new StreamLogger("Logger Y: "))))
								.to(module(new StreamLogger("Logger Z: ")))
						.join(new NaiveStreamJoiner())
						.followedBy(module(new FormetaEncoder()))
						.dispatchWith(new DuplicateObjectsStrategy<>())
								.to(map(obj -> "RECORD1: " + obj))
								.to(map(obj -> "RECORD2: " + obj))
						.join(new NaiveObjectsJoiner<>());

		final List<String> records =
				Flux.process("1{lit1: val1}\n2{lit2: val2}")
						.toObjectsWith(myFlow)
						.collectResults();

		records.stream()
				.forEach(System.out::println);

		Flux.process("1{lit1: val1}\n2{lit2: val2}")
				.toObjectsWith(myFlow2)
				.collectResults()
				.stream()
						.forEach(System.out::println);
	}

}
