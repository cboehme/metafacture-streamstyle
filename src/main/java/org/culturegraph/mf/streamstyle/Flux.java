package org.culturegraph.mf.streamstyle;

import org.culturegraph.mf.framework.Receiver;

/**
 * @author Christoph Böhme
 */
public class Flux {

	public static <T> ObjectSenderHead<T, T> process(final T input) {
		final FlowStarter<T> flowStarter = new FlowStarter<>(input);
		return new ObjectSenderHead<>(new AbstractHead<T>(flowStarter), flowStarter);
	}

	public static <R extends Receiver, S extends Receiver> Branch<R, S> branch(final Module<R, S> module) {
		return new Branch<>(module);
	}

}
