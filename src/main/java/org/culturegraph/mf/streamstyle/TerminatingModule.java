package org.culturegraph.mf.streamstyle;

import org.culturegraph.mf.framework.Receiver;

/**
 * @author Christoph Böhme
 */
public class TerminatingModule<R extends Receiver> {

	private final R receiver;

	private TerminatingModule(final R receiver) {
		this.receiver = receiver;
	}

	public R getReceiver() {
		return receiver;
	}

	public static <R extends Receiver> TerminatingModule<R> module(
			final R receiver) {
		return new TerminatingModule<>(receiver);
	}

}
