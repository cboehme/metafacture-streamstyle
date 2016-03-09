package org.culturegraph.mf.streamstyle;

import org.culturegraph.mf.framework.Receiver;

/**
 * @author Christoph Böhme
 */
public interface DispatcherStrategy<R extends Receiver> {

	R getReceiver();

	void addFlow(R firstModule);

}
