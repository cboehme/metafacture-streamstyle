package org.culturegraph.mf.streamstyle;

import org.culturegraph.mf.framework.DefaultSender;
import org.culturegraph.mf.framework.ObjectReceiver;

/**
 * @author Christoph Böhme
 */
class FlowStarter<T> extends DefaultSender<ObjectReceiver<T>> {

	private final T input;

	public FlowStarter(final T input) {
		this.input = input;
	}

	public void run() {
		getReceiver().process(input);
		getReceiver().closeStream();
	}

}
