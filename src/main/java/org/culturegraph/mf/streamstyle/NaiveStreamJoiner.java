package org.culturegraph.mf.streamstyle;

import org.culturegraph.mf.framework.Sender;
import org.culturegraph.mf.framework.StreamPipe;
import org.culturegraph.mf.framework.StreamReceiver;
import org.culturegraph.mf.stream.pipe.IdentityStreamPipe;

/**
 * @author Christoph Böhme
 */
public class NaiveStreamJoiner implements JoinStrategy<StreamReceiver> {

	private final StreamPipe<StreamReceiver> passThrough = new IdentityStreamPipe();

	@Override
	public Sender<StreamReceiver> getSender() {
		return passThrough;
	}

	@Override
	public void addFlow(final Sender<StreamReceiver> lastModule) {
		lastModule.setReceiver(passThrough);
	}

}
