package org.culturegraph.mf.streamstyle;

import static org.culturegraph.mf.streamstyle.Module.module;

import java.util.function.Function;

import org.culturegraph.mf.framework.DefaultObjectPipe;
import org.culturegraph.mf.framework.ObjectReceiver;

/**
 * @author Christoph Böhme
 */
public class InlineModules {

	private InlineModules() {
		// no instances allowed
	}

	public static <T, U> Module<ObjectReceiver<T>, ObjectReceiver<U>> flatMap(
			final Function<T, Iterable<U>> function) {
		return module(new DefaultObjectPipe<T, ObjectReceiver<U>>() {
			@Override
			public void process(final T object) {
				function.apply(object).forEach(getReceiver()::process);
			}
		});
	}
}
