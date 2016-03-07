package org.culturegraph.mf.streamstyle;

import static java.util.Arrays.asList;
import static org.culturegraph.mf.streamstyle.Flux.branch;
import static org.culturegraph.mf.streamstyle.Module.module;

import java.util.List;

import org.culturegraph.mf.stream.converter.FormetaDecoder;
import org.culturegraph.mf.stream.converter.FormetaEncoder;
import org.culturegraph.mf.stream.pipe.StreamLogger;

public class Main {

	public static void main(final String[] argv) {

		final List<String> records = Flux.process("1{lit1: val1}\n2{lit2: val2}")
				.flatMap(obj -> asList(obj.split("\n")))
				.toStream(new FormetaDecoder())
				.fork(
						branch(module(new StreamLogger("1a"))).processWith(module(new StreamLogger("1b"))),
						branch(module(new StreamLogger("2a"))).processWith(module(new StreamLogger("2b"))))
				.join()
				.toObjects(new FormetaEncoder())
				.collect();

		records.stream()
				.forEach(System.out::println);
	}

}
