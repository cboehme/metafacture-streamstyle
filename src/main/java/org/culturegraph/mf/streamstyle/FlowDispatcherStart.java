package org.culturegraph.mf.streamstyle;

import org.culturegraph.mf.framework.Receiver;

/**
 * @author Christoph Böhme
 */
public class FlowDispatcherStart<F extends Receiver, L extends Receiver> {

	final Flow<F, L> oldFlow;

	final DispatcherStrategy<L> dispatcherStrategy;

	FlowDispatcherStart(final Flow<F, L> oldFlow,
			final DispatcherStrategy<L> dispatcherStrategy) {
		this.oldFlow = oldFlow;
		this.dispatcherStrategy = dispatcherStrategy;
	}

	public <N extends Receiver> FlowDispatcher<F, L, N> to(final Module<L, N> module) {
		return to(Flow.startWith(module));
	}

	public <N extends Receiver> FlowDispatcher<F, L, N> to(final Flow<L, N> flow) {
		final FlowDispatcher<F, L, N> flowDispatcher = new FlowDispatcher<>(this);
		return flowDispatcher.to(flow);
	}

}
