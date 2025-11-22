package org.dorkmaster.pipeline;

public interface Pipeline {
    PipelineContext execute(PipelineContext ctx);
}
