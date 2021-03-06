package org.culturegraph.mf.streamstyle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.culturegraph.mf.framework.LifeCycle;
import org.culturegraph.mf.framework.ObjectReceiver;

/**
 * @author Christoph Böhme
 */
public class DuplicateObjectsStrategy<T> implements DispatcherStrategy<ObjectReceiver<T>, Void> {

	private final List<ObjectReceiver<T>> receivers = new ArrayList<>();
	private final ObjectReceiver<T> duplicator = new ObjectDuplicator();

	@Override
	public ObjectReceiver<T> getReceiver() {
		return duplicator;
	}

	@Override
	public void addFlow(final Predicate<Void> condition,
			final ObjectReceiver<T> firstModule) {
		receivers.add(firstModule);
	}

	private class ObjectDuplicator implements ObjectReceiver<T> {

		@Override
		public void process(final T object) {
			receivers.forEach(receiver -> receiver.process(object));
		}

		@Override
		public void resetStream() {
			receivers.forEach(LifeCycle::resetStream);
		}

		@Override
		public void closeStream() {
			receivers.forEach(LifeCycle::closeStream);
		}

	}

}
