package org.culturegraph.mf.streamstyle;

import static java.util.Arrays.stream;

import org.culturegraph.mf.framework.StreamPipe;
import org.culturegraph.mf.framework.StreamReceiver;
import org.culturegraph.mf.stream.pipe.IdentityStreamPipe;

/**
 * @author Christoph Böhme
 */
public class JoinStreamHead<F> extends AbstractHead<F> {

	private final Branch<StreamReceiver, StreamReceiver>[] branches;

	protected JoinStreamHead(final AbstractHead<F> currentHead,
			final Branch<StreamReceiver, StreamReceiver>... branches) {
		super(currentHead);
		this.branches = branches;
	}

	public StreamSenderHead<F> join() {
		final StreamPipe<StreamReceiver> streamJoiner = new IdentityStreamPipe();
		stream(branches)
				.map(Branch::getLastModule)
				.forEach(branchEnd -> branchEnd.setReceiver(streamJoiner));
		return new StreamSenderHead<>(this, streamJoiner);
	}

}
