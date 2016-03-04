package org.culturegraph.mf.streamstyle;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.culturegraph.mf.framework.DefaultObjectPipe;
import org.culturegraph.mf.framework.ObjectPipe;
import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.framework.Sender;
import org.culturegraph.mf.framework.StreamReceiver;

/**
 * @author Christoph Böhme
 */
public class ObjectSenderHead<F, T> extends AbstractHead<F> {

	private final Sender<ObjectReceiver<T>> headModule;

	ObjectSenderHead(final AbstractHead<F> currentHead,
			final Sender<ObjectReceiver<T>> headModule) {
		super(currentHead);
		this.headModule = headModule;
	}

	public <E> ObjectSenderHead<F, E> map(final Function<T, E> function) {
		return with(new DefaultObjectPipe<T, ObjectReceiver<E>>() {
			@Override
			public void process(final T obj) {
				getReceiver().process(function.apply(obj));
			}
		});
	}

	public <E> ObjectSenderHead<F, E> flatMap(final Function<T, Iterable<E>> function) {
		return with(new DefaultObjectPipe<T, ObjectReceiver<E>>() {
			@Override
			public void process(final T obj) {
				function.apply(obj).forEach(getReceiver()::process);
			}
		});
	}

	public StreamSenderHead<F> transform(final BiConsumer<T, StreamReceiver> transformer) {
		return toStream(new DefaultObjectPipe<T, StreamReceiver>() {
			@Override
			public void process(final T obj) {
				transformer.accept(obj, getReceiver());
			}
		});
	}

	public <E> ObjectSenderHead<F, E> with(final ObjectPipe<T, ObjectReceiver<E>> module) {
		headModule.setReceiver(module);
		return new ObjectSenderHead<>(this, module);
	}

	public StreamSenderHead<F> toStream(final ObjectPipe<T, StreamReceiver> module) {
		headModule.setReceiver(module);
		return new StreamSenderHead<>(this, module);
	}

	public List<T> collect() {
		final Collector<T> collector = new Collector<>();
		headModule.setReceiver(collector);
		getFlowStarter().run();
		return collector.getCollectedObjects();
	}

}
