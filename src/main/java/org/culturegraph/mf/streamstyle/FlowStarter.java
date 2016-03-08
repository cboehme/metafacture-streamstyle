package org.culturegraph.mf.streamstyle;

import org.culturegraph.mf.framework.DefaultObjectPipe;
import org.culturegraph.mf.framework.ObjectReceiver;

/**
 * @author Christoph Böhme
 */
class FlowStarter<T> extends DefaultObjectPipe<T, ObjectReceiver<T>> {

	private final T input;

	public FlowStarter(final T input) {
		this.input = input;
	}

	@Override
	public void process(final T obj) {
		run();
	}

	public void run() {
		getReceiver().process(input);
		getReceiver().closeStream();
	}

}
