package org.culturegraph.mf.streamstyle;

/**
 * @author Christoph Böhme
 */
public class Flux {

	public static <T> ObjectSenderHead<T, T> process(final T input) {
		final FlowStarter<T> flowStarter = new FlowStarter<>(input);
		return new ObjectSenderHead<>(new AbstractHead<T>(flowStarter), flowStarter);
	}

	public static <T> StreamSenderHead<T> branch() {
		return null;
	}

}
