package org.culturegraph.mf.streamstyle;

import java.util.function.Predicate;

import org.culturegraph.mf.framework.Receiver;

/**
 * @author Christoph Böhme
 */
public class TerminatedFlowDispatcher<F extends Receiver, L extends Receiver, P> {

	private final Flow<F, L> oldFlow;

	private final DispatcherStrategy<L, P> dispatcherStrategy;

	TerminatedFlowDispatcher(
			final FlowDispatcherStart<F, L, P> flowDispatcherStart) {
		oldFlow = flowDispatcherStart.oldFlow;
		dispatcherStrategy = flowDispatcherStart.dispatcherStrategy;
	}

	public TerminatedFlowDispatcher<F, L, P> to(
			final TerminatingModule<L> module) {
		return to(Flow.startWith(module));
	}

	public TerminatedFlowDispatcher<F, L, P> to(
			final TerminatedFlow<L, ?> flow) {
		return to(x -> true, flow);
	}

	public TerminatedFlowDispatcher<F, L, P> to(Predicate<P> condition,
			final TerminatingModule<L> module) {
		return to(condition, Flow.startWith(module));
	}

	public TerminatedFlowDispatcher<F, L, P> to(Predicate<P> condition,
			final TerminatedFlow<L, ?> flow) {
		dispatcherStrategy.addFlow(condition, flow.getFirstModule());
		return this;
	}

	public TerminatedFlow<F, L> terminate() {
		return new TerminatedFlow<F, L>(oldFlow);
	}

}
