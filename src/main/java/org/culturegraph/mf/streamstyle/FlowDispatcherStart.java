package org.culturegraph.mf.streamstyle;

import java.util.function.Predicate;

import org.culturegraph.mf.framework.Receiver;

/**
 * @author Christoph Böhme
 */
public class FlowDispatcherStart<F extends Receiver, L extends Receiver, P> {

	final Flow<F, L> oldFlow;

	final DispatcherStrategy<L, P> dispatcherStrategy;

	FlowDispatcherStart(final Flow<F, L> oldFlow,
			final DispatcherStrategy<L, P> dispatcherStrategy) {
		this.oldFlow = oldFlow;
		this.dispatcherStrategy = dispatcherStrategy;
	}

	public <N extends Receiver> FlowDispatcher<F, L, N, P> to(final Module<L, N> module) {
		return to(Flow.startWith(module));
	}

	public <N extends Receiver> FlowDispatcher<F, L, N, P> to(final Flow<L, N> flow) {
		return to(x -> true, flow);
	}

	public <N extends Receiver> FlowDispatcher<F, L, N, P> to(Predicate<P> condition,
			final Module<L, N> module) {
		return to(condition, Flow.startWith(module));
	}

	public <N extends Receiver> FlowDispatcher<F, L, N, P> to(Predicate<P> condition,
			final Flow<L, N> flow) {
		final FlowDispatcher<F, L, N, P> flowDispatcher = new FlowDispatcher<>(this);
		return flowDispatcher.to(condition, flow);
	}

	public TerminatedFlowDispatcher<F, L, P> to(final TerminatingModule<L> module) {
		return to(Flow.startWith(module));
	}

	public TerminatedFlowDispatcher<F, L, P> to(final TerminatedFlow<L, ?> flow) {
		return to(x -> true, flow);
	}

	public TerminatedFlowDispatcher<F, L, P> to(Predicate<P> condition,
			final TerminatingModule<L> module) {
		return to(condition, Flow.startWith(module));
	}

	public TerminatedFlowDispatcher<F, L, P> to(Predicate<P> condition,
			final TerminatedFlow<L, ?> flow) {
		final TerminatedFlowDispatcher<F, L, P> flowDispatcher = new TerminatedFlowDispatcher<>(this);
		return flowDispatcher.to(condition, flow);
	}

}
