package org.culturegraph.mf.streamstyle;

import org.culturegraph.mf.framework.Receiver;
import org.culturegraph.mf.framework.Sender;

/**
 * @author Christoph Böhme
 */
public interface JoinStrategy<R extends Receiver> {

	Sender<R> getSender();

	void addFlow(final Sender<R> lastModule);

}
