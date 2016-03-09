package org.culturegraph.mf.streamstyle;

import org.culturegraph.mf.framework.Receiver;

/**
 * @author Christoph Böhme
 */
public class TerminatedFlow<F extends Receiver, L extends Receiver>  {

	private final F firstModule;
	private final TerminatingModule<L> finalModule;

	TerminatedFlow(final Flow<F, L> oldFlow) {
		this.firstModule = oldFlow.getFirstModule();
		this.finalModule = null;
	}

	TerminatedFlow(final Flow<F, L> oldFlow,
			final TerminatingModule<L> finalModule) {
		this.firstModule = oldFlow.getFirstModule();
		this.finalModule = finalModule;
	}

	TerminatedFlow(final TerminatingModule<F> firstModule,
			TerminatingModule<L> lastModule) {
		this.firstModule = firstModule.getReceiver();
		this.finalModule = lastModule;
	}

	public F getFirstModule() {
		return firstModule;
	}

	/**
	 * Returns the last module of the flow.
	 * @return the last module. May be null if the flow ends with a dispatcher.
	 */
	public TerminatingModule<L> getFinalModule() {
		return finalModule;
	}

}
