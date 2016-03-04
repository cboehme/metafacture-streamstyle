package org.culturegraph.mf.streamstyle;

import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.framework.StreamPipe;
import org.culturegraph.mf.framework.StreamReceiver;
import org.culturegraph.mf.stream.pipe.StreamTee;

/**
 * @author Christoph Böhme
 */
public class ForkedStreamSenderHead<F> extends AbstractHead<F> {

	private final StreamTee headModule;

	ForkedStreamSenderHead(final AbstractHead<F> currentHead,
			final StreamTee headModule) {
		super(currentHead);
		this.headModule = headModule;
	}

	public StreamSenderHead<F> with(final StreamPipe<StreamReceiver> module) {
		headModule.addReceiver(module);
		return new StreamSenderHead<>(this, module);
	}

	public <E> ObjectSenderHead<F, E> toObjects(
			final StreamPipe<ObjectReceiver<E>> module) {
		headModule.addReceiver(module);
		return new ObjectSenderHead<>(this, module);
	}

	public StreamForkHead<F> fork() {
		final StreamTee streamTee = new StreamTee();
		headModule.addReceiver(streamTee);
		return new StreamForkHead<>(this, streamTee);
	}

}
