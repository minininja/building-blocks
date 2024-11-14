package org.dorkmaster.pipeline.parallel;

import org.dorkmaster.pipeline.PipelineContext;
import org.dorkmaster.pipeline.Stage;
import org.dorkmaster.pipeline.standard.Pipeline;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParallelPipelineTests {

    @Test
    public void testHappy() {
        PipelineContext ctx = PipelineContext.newInstance().set("one", 1).set("two", 2);
        Pipeline pipeline = new ParallelPipeline()
                .addStage(new Stage() {
                    @Override
                    public Collection<String> required() {
                        return List.of("one");
                    }

                    @Override
                    public Collection<String> provided() {
                        return List.of();
                    }

                    @Override
                    public PipelineContext execute(PipelineContext ctx) {
                        return PipelineContext.newInstance().set("three", 3);
                    }
                })
                .addStage(new Stage() {
                    @Override
                    public Collection<String> required() {
                        return List.of("two");
                    }

                    @Override
                    public Collection<String> provided() {
                        return List.of();
                    }

                    @Override
                    public PipelineContext execute(PipelineContext ctx) {
                        return PipelineContext.newInstance().set("four", 4);
                    }
                })
                .addStage(new Stage() {
                    @Override
                    public Collection<String> required() {
                        return List.of("one", "two");
                    }

                    @Override
                    public Collection<String> provided() {
                        return List.of();
                    }

                    @Override
                    public PipelineContext execute(PipelineContext ctx) {
                        return PipelineContext.newInstance().set("five", 5);
                    }
                })
                .addStage(new Stage() {
                    @Override
                    public Collection<String> required() {
                        return List.of("three", "four", "five");
                    }

                    @Override
                    public Collection<String> provided() {
                        return List.of();
                    }

                    @Override
                    public PipelineContext execute(PipelineContext ctx) {
                        return PipelineContext.newInstance().set("six", 6);
                    }
                });

        PipelineContext result = pipeline.execute(ctx);

        assertNotNull(result.get("one"));
        assertNotNull(result.get("two"));
        assertNotNull(result.get("three"));
        assertNotNull(result.get("four"));
        assertNotNull(result.get("five"));
        assertNotNull(result.get("six"));
    }

    @Test
    public void testUnresolvable() {
        PipelineContext ctx = PipelineContext.newInstance();
        Pipeline pipeline = new ParallelPipeline()
                .addStage(new Stage() {
                    @Override
                    public Collection<String> required() {
                        return List.of("one");
                    }

                    @Override
                    public Collection<String> provided() {
                        return List.of();
                    }

                    @Override
                    public PipelineContext execute(PipelineContext ctx) {
                        return PipelineContext.newInstance().set("three", 3);
                    }
                });

        assertThrows(UnresolvableRequirementsException.class, () -> {pipeline.execute(ctx);});
    }
}
