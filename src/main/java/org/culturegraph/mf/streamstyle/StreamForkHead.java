package org.culturegraph.mf.streamstyle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.culturegraph.mf.stream.pipe.StreamTee;

/**
 * @author Christoph Böhme
 */
public class StreamForkHead<F> extends AbstractHead<F> {

	private ForkedStreamSenderHead<F> forkedStreamSenderHeader;
	private List<StreamSenderHead<F>> forkHeads = new ArrayList<>();

	StreamForkHead(final AbstractHead<F> currentHead,
			final StreamTee forkModule) {
		super(currentHead);
		forkedStreamSenderHeader = new ForkedStreamSenderHead<>(this, forkModule);
	}

	public StreamForkHead<F> to(
			final Function<ForkedStreamSenderHead<F>, StreamSenderHead<F>> fluxBuilder) {
		forkHeads.add(fluxBuilder.apply(forkedStreamSenderHeader));
		return this;
	}

	public StreamSenderHead<F> join() {
		final StreamJoiner joiner = new StreamJoiner();
		forkHeads.forEach(head -> head.with(joiner));
		return new StreamSenderHead<>(this, joiner);
	}

}
