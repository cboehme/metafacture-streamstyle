package org.culturegraph.mf.streamstyle;

import org.culturegraph.mf.framework.Receiver;
import org.culturegraph.mf.framework.Sender;

/**
 * @author Christoph Böhme
 */
public class Flow<F extends Receiver, L extends Receiver> {

	private final F firstModule;
	private final Sender<L> lastModule;

	private Flow(final Module<F, L> module) {
		firstModule = module.getReceiver();
		lastModule = module.getSender();
	}

	// TODO: Try to use private as access modifier
	<P extends Receiver> Flow(final Flow<F, P> oldFlow,
			final Sender<L> newModule) {
		firstModule = oldFlow.firstModule;
		lastModule = newModule;
	}

	public F getFirstModule() {
		return firstModule;
	}

	public Sender<L> getLastModule() {
		return lastModule;
	}

	public <N extends Receiver> Flow<F, N> followedBy(final Module<L, N> module) {
		lastModule.setReceiver(module.getReceiver());
		return new Flow<>(this, module.getSender());
	}

	public <N extends Receiver> FlowDispatcher<F, L, N> dispatchWith(
			final DispatcherStrategy<L> dispatcherStrategy,
			final JoinStrategy<N> joinStrategy) {
		lastModule.setReceiver(dispatcherStrategy.getReceiver());
		return new FlowDispatcher<>(this, dispatcherStrategy, joinStrategy);
	}

	public static <R extends Receiver, S extends Receiver> Flow<R, S> startWith(
			final Module<R, S> module)  {
		return new Flow<>(module);
	}

}
