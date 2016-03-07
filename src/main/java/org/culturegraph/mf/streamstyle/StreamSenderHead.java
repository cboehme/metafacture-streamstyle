package org.culturegraph.mf.streamstyle;

import static java.util.Arrays.stream;

import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.framework.Sender;
import org.culturegraph.mf.framework.StreamPipe;
import org.culturegraph.mf.framework.StreamReceiver;
import org.culturegraph.mf.stream.pipe.StreamTee;

/**
 * @author Christoph Böhme
 */
public class StreamSenderHead<F> extends AbstractHead<F> {

	private final Sender<StreamReceiver> headModule;

	StreamSenderHead(final AbstractHead<F> currentHead,
			final Sender<StreamReceiver> headModule) {
		super(currentHead);
		this.headModule = headModule;
	}

	public StreamSenderHead<F> with(final StreamPipe<StreamReceiver> module) {
		headModule.setReceiver(module);
		return new StreamSenderHead<>(this, module);
	}

	public <E> ObjectSenderHead<F, E> toObjects(
			final StreamPipe<ObjectReceiver<E>> module) {
		headModule.setReceiver(module);
		return new ObjectSenderHead<>(this, module);
	}

	@SafeVarargs
	public final JoinStreamHead<F> fork(
			final Branch<StreamReceiver, StreamReceiver>... branches) {
		final StreamTee streamTee = new StreamTee();
		stream(branches)
				.map(Branch::getFirstModule)
				.forEach(streamTee::addReceiver);
		headModule.setReceiver(streamTee);
		return new JoinStreamHead<>(this, branches);
	}

}
