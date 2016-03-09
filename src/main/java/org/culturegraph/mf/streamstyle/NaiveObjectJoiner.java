package org.culturegraph.mf.streamstyle;

import org.culturegraph.mf.framework.DefaultObjectPipe;
import org.culturegraph.mf.framework.ObjectPipe;
import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.framework.Sender;

/**
 * @author Christoph Böhme
 */
public class NaiveObjectJoiner<T> implements JoinStrategy<ObjectReceiver<T>> {

	private ObjectPipe<T, ObjectReceiver<T>> passThrough = new IdentityObjectPipe();

	@Override
	public Sender<ObjectReceiver<T>> getSender() {
		return passThrough;
	}

	@Override
	public void addFlow(final Sender<ObjectReceiver<T>> lastModule) {
		lastModule.setReceiver(passThrough);
	}

	private class IdentityObjectPipe extends DefaultObjectPipe<T, ObjectReceiver<T>> {

		@Override
		public void process(final T object) {
			getReceiver().process(object);
		}

	}

}
