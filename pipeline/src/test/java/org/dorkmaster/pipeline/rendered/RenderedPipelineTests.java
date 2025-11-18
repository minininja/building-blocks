package org.dorkmaster.pipeline.rendered;

import org.dorkmaster.pipeline.BasePipelineContext;
import org.dorkmaster.pipeline.PipelineContext;
import org.dorkmaster.pipeline.Stage;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RenderedPipelineTests {
    @Test
    public void testHappyRendered() {
        RenderedPipeline<Object> pipeline = new RenderedPipeline<>()
                .addStage(new Stage() {
                    @Override
                    public Collection<String> required() {
                        return List.of("a");
                    }

                    @Override
                    public Collection<String> provides() {
                        return List.of("b");
                    }

                    @Override
                    public PipelineContext execute(PipelineContext ctx) {
                        return ctx.set("b", "b");
                    }
                })
                .setRenderer(new PipelineContextRenderer() {
                    @Override
                    public Object render(PipelineContext context) {
                        return context.get("b");
                    }
                });

        Object result = pipeline.execute(new BasePipelineContext().set("a", "a"));
        assertEquals(result, "b");
    }
}
