package org.dorkmaster.pipeline.exception;

import org.dorkmaster.pipeline.PipelineContext;

public class PipelineExecutionException extends PipelineException {
    private PipelineContext ctx;

    public PipelineExecutionException(String message, PipelineContext ctx) {
        super(message);
    }

    public PipelineContext getCtx() {
        return ctx;
    }
}
