package org.culturegraph.mf.streamstyle;

import org.culturegraph.mf.framework.ObjectPipe;
import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.framework.Receiver;
import org.culturegraph.mf.framework.Sender;
import org.culturegraph.mf.framework.StreamPipe;
import org.culturegraph.mf.framework.StreamReceiver;
import org.culturegraph.mf.framework.XmlPipe;
import org.culturegraph.mf.framework.XmlReceiver;

/**
 * @author Christoph Böhme
 */
public class Module<R extends Receiver, S extends Receiver> {

	private final R receiver;
	private final Sender<S> sender;

	private Module(final R receiver, final Sender<S> sender) {
		this.receiver = receiver;
		this.sender = sender;
	}

	public R getReceiver() {
		return receiver;
	}

	public Sender<S> getSender() {
		return sender;
	}

	public static <U extends Receiver> Module<StreamReceiver, U> module(final StreamPipe<U> streamPipe) {
		return new Module<>(streamPipe, streamPipe);
	}

	public static <T, U extends Receiver> Module<ObjectReceiver<T>, U> module(final ObjectPipe<T, U> objectPipe) {
		return new Module<>(objectPipe, objectPipe);
	}

	public static <U extends Receiver> Module<XmlReceiver, U> module(final XmlPipe<U> xmlPipe) {
		return new Module<>(xmlPipe, xmlPipe);
	}

}
