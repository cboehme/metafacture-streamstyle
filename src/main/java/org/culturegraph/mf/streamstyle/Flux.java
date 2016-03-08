package org.culturegraph.mf.streamstyle;

import static org.culturegraph.mf.streamstyle.Module.module;

import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.framework.Receiver;

/**
 * @author Christoph Böhme
 */
public class Flux {

	public static <T> Branch<ObjectReceiver<T>, ObjectReceiver<T>> process(final T input) {
		final FlowStarter<T> flowStarter = new FlowStarter<>(input);
		return new Branch(module(flowStarter));
	}

	public static <R extends Receiver, S extends Receiver> Branch<R, S> branch()  {
		return new Branch<>();
	}

}
