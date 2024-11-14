package org.dorkmaster.pipeline.parallel;

import org.dorkmaster.pipeline.PipelineContext;
import org.dorkmaster.pipeline.PipelineException;

public class UnresolvableRequirementsException extends PipelineException {
    private int executedStages;

    public UnresolvableRequirementsException(String message, PipelineContext ctx, int executedStages) {
        super(message, ctx);
        this.executedStages = executedStages;
    }

    public int getExecutedStages() {
        return executedStages;
    }
}
