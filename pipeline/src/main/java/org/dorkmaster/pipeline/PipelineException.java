package org.dorkmaster.pipeline;

public class PipelineException extends RuntimeException {
    private PipelineContext ctx;

    public PipelineException(String message, PipelineContext ctx) {
        super(message);
        this.ctx = ctx;
    }

    public PipelineContext getCtx() {
        return ctx;
    }
}
