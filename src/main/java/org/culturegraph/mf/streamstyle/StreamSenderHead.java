package org.culturegraph.mf.streamstyle;

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

	public StreamForkHead<F> fork() {
		final StreamTee streamTee = new StreamTee();
		headModule.setReceiver(streamTee);
		return new StreamForkHead<>(this, streamTee);
	}

}
