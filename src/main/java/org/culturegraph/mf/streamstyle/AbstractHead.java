package org.culturegraph.mf.streamstyle;

/**
 * @author Christoph Böhme
 */
public class AbstractHead<F> {

	private final FlowStarter<F> flowStarter;

	protected AbstractHead(AbstractHead<F> currentHead) {
		this.flowStarter = currentHead.flowStarter;
	}

	protected AbstractHead(FlowStarter<F> flowStarter) {
		this.flowStarter = flowStarter;
	}

	protected FlowStarter<F> getFlowStarter() {
		return flowStarter;
	}

}
