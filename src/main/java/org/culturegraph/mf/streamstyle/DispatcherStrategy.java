package org.culturegraph.mf.streamstyle;

import java.util.function.Predicate;

import org.culturegraph.mf.framework.Receiver;

/**
 * @author Christoph Böhme
 */
public interface DispatcherStrategy<R extends Receiver, P> {

	R getReceiver();

	void addFlow(final Predicate<P> condition, R firstModule);

}
