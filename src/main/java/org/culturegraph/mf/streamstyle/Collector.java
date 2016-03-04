package org.culturegraph.mf.streamstyle;

import java.util.ArrayList;
import java.util.List;

import org.culturegraph.mf.framework.DefaultObjectReceiver;

/**
 * @author Christoph Böhme
 */
class Collector<T> extends DefaultObjectReceiver<T> {

	private final List<T> collectedObjects = new ArrayList<>();

	public List<T> getCollectedObjects() {
		return collectedObjects;
	}

	@Override
	public void process(final T object) {
		collectedObjects.add(object);
	}

}
