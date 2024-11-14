package org.dorkmaster.pipeline.rendered;

import org.dorkmaster.pipeline.PipelineContext;

public interface PipelineContextRenderer<T> {
    T render(PipelineContext context);
}
