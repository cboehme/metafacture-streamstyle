package org.culturegraph.mf.streamstyle;

import org.culturegraph.mf.framework.Receiver;
import org.culturegraph.mf.framework.Sender;

/**
 * @author Christoph Böhme
 */
public class Branch<F extends Receiver, L extends Receiver> {

	private final F firstModule;
	private final Sender<L> lastModule;

	Branch(final Module<F, L> module) {
		firstModule = module.getReceiver();
		lastModule = module.getSender();
	}

	private <P extends Receiver> Branch(final Branch<F, P> oldBranch,
			final Sender<L> newModule) {
		firstModule = oldBranch.firstModule;
		lastModule = newModule;
	}

	public F getFirstModule() {
		return firstModule;
	}

	public Sender<L> getLastModule() {
		return lastModule;
	}

	public <N extends Receiver> Branch<F, N> processWith(final Module<L, N> module) {
		lastModule.setReceiver(module.getReceiver());
		return new Branch<>(this, module.getSender());
	}

}
