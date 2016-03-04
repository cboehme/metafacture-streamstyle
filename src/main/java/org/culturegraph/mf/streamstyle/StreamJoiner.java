package org.culturegraph.mf.streamstyle;

import org.culturegraph.mf.framework.DefaultStreamPipe;
import org.culturegraph.mf.framework.StreamReceiver;

/**
 * @author Christoph Böhme
 */
public class StreamJoiner extends DefaultStreamPipe<StreamReceiver> {

	@Override
	public void startRecord(final String identifier) {
		getReceiver().startRecord(identifier);
	}

	@Override
	public void endRecord() {
		getReceiver().endRecord();
	}

	@Override
	public void startEntity(final String name) {
		getReceiver().startEntity(name);
	}

	@Override
	public void endEntity() {
		getReceiver().endEntity();
	}

	@Override
	public void literal(final String name, final String value) {
		getReceiver().literal(name, value);
	}

}
