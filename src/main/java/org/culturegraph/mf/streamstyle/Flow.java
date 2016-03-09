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
		this.firstModule = module.getReceiver();
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

	public TerminatedFlow<F, L> followedBy(final TerminatingModule<L> module) {
		lastModule.setReceiver(module.getReceiver());
		return new TerminatedFlow<>(this, module);
	}

	public <P> FlowDispatcherStart<F, L, P> dispatchWith(
			final DispatcherStrategy<L, P> dispatcherStrategy) {
		lastModule.setReceiver(dispatcherStrategy.getReceiver());
		return new FlowDispatcherStart<>(this, dispatcherStrategy);
	}

	public static <R extends Receiver, S extends Receiver> Flow<R, S> startWith(
			final Module<R, S> module)  {
		return new Flow<>(module);
	}

	public static <R extends Receiver> TerminatedFlow<R, R> startWith(
			final TerminatingModule<R> module) {
		return new TerminatedFlow<>(module, module);
	}

}
