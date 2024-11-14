package org.dorkmaster.pipeline.rendered;

import org.dorkmaster.pipeline.PipelineContext;
import org.dorkmaster.pipeline.Stage;
import org.dorkmaster.pipeline.standard.Pipeline;

public class RenderedPipeline<T> {
    private PipelineContextRenderer<T> renderer;
    private Pipeline pipeline = new Pipeline();

    public RenderedPipeline<T> addStage(Stage stage) {
        pipeline.addStage(stage);
        return this;
    }

    public RenderedPipeline<T> setRenderer(PipelineContextRenderer<T> renderer) {
        this.renderer = renderer;
        return this;
    };

    public T execute(PipelineContext ctx) {
        return renderer.render(pipeline.execute(ctx));
    }
}
