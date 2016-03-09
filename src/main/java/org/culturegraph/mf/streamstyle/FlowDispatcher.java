package org.culturegraph.mf.streamstyle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.culturegraph.mf.framework.Receiver;

public class FlowDispatcher<F extends Receiver, L extends Receiver, N extends Receiver, P> {

	private final Flow<F, L> oldFlow;

	private final DispatcherStrategy<L, P> dispatcherStrategy;

	private final List<Flow<L, N>> flows = new ArrayList<>();

	FlowDispatcher(final FlowDispatcherStart<F, L, P> flowDispatcherStart) {
		this.oldFlow = flowDispatcherStart.oldFlow;
		this.dispatcherStrategy = flowDispatcherStart.dispatcherStrategy;
	}

	public FlowDispatcher<F, L, N, P> to(final Module<L, N> module) {
		return to(Flow.startWith(module));
	}

	public FlowDispatcher<F, L, N, P> to(final Flow<L, N> flow) {
		return to(x -> true, flow);
	}

	public FlowDispatcher<F, L, N, P> to(Predicate<P> condition,
			final Module<L, N> module) {
		return to(condition, Flow.startWith(module));
	}

	public FlowDispatcher<F, L, N, P> to(Predicate<P> condition,
			final Flow<L, N> flow) {
		dispatcherStrategy.addFlow(condition, flow.getFirstModule());
		flows.add(flow);
		return this;
	}

	public Flow<F, N> join(final JoinStrategy<N> joinStrategy) {
		flows.forEach(flow -> joinStrategy.addFlow(flow.getLastModule()));
		return new Flow<>(oldFlow, joinStrategy.getSender());
	}

}
