package org.culturegraph.mf.streamstyle;

import java.util.List;

import org.culturegraph.mf.framework.DefaultObjectReceiver;
import org.culturegraph.mf.framework.DefaultStreamReceiver;
import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.framework.Sender;
import org.culturegraph.mf.framework.StreamReceiver;
import org.culturegraph.mf.stream.sink.EventList;

/**
 * @author Christoph Böhme
 */
public class Flux {

	public static <T> ProcessObject<T> process(final T input) {
		return new ProcessObject<>(input);
	}

	public static class ProcessObject<T> {

		private final T input;

		ProcessObject(final T input) {
			this.input = input;
		}

		public <U> ObjectReceivingTerminator<U> toObjectsWith(
				final Flow<ObjectReceiver<T>, ObjectReceiver<U>> flow) {
			return new ObjectReceivingTerminator<>(
					new ObjectSendingStarter<>(input, flow.getFirstModule()),
					flow.getLastModule());
		}

		public StreamReceivingTerminator toStreamWith(
				final Flow<ObjectReceiver<T>, StreamReceiver> flow) {
			return new StreamReceivingTerminator(
					new ObjectSendingStarter<>(input, flow.getFirstModule()),
					flow.getLastModule());
		}

	}

	private interface Starter {

		void run();

	}

	private static class ObjectSendingStarter<T> implements Starter {

		private final T input;
		private final ObjectReceiver<T> flowStart;

		private ObjectSendingStarter(final T input, ObjectReceiver<T> flowStart) {
			this.input = input;
			this.flowStart = flowStart;
		}

		@Override
		public void run() {
			flowStart.process(input);
		}

	}

	public static class ObjectReceivingTerminator<T> {

		private final Starter starter;
		private final Sender<ObjectReceiver<T>> flowEnd;

		ObjectReceivingTerminator(final Starter starter,
				Sender<ObjectReceiver<T>> flowEnd) {
			this.starter = starter;
			this.flowEnd = flowEnd;
		}

		public List<T> collectResults() {
			final Collector<T> collector = new Collector<>();
			flowEnd.setReceiver(collector);
			starter.run();
			return collector.getCollectedObjects();
		}

		public void discardResults() {
			flowEnd.setReceiver(new DefaultObjectReceiver<>());
			starter.run();
		}

	}

	private static class StreamReceivingTerminator {
		private final Starter starter;
		private final Sender<StreamReceiver> flowEnd;

		public StreamReceivingTerminator(
				final Starter starter,
				final Sender<StreamReceiver> flowEnd) {

			this.starter = starter;
			this.flowEnd = flowEnd;
		}

		public EventList collectResults() {
			// TODO: Do not return module but only the event stream
			final EventList eventList = new EventList();
			flowEnd.setReceiver(eventList);
			starter.run();
			return eventList;
		}

		public void discardResults() {
			flowEnd.setReceiver(new DefaultStreamReceiver());
			starter.run();
		}

	}

}
