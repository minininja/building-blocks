package org.dorkmaster.pipeline.parallel;

import org.dorkmaster.pipeline.Pipeline;
import org.dorkmaster.pipeline.PipelineContext;
import org.dorkmaster.pipeline.Stage;
import org.dorkmaster.pipeline.exception.ThreadTimeoutExceededException;
import org.dorkmaster.pipeline.util.TestStage;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractThreadTests {
    protected abstract Pipeline create(Collection<Stage> stages);
    protected abstract Pipeline create(Collection<Stage> stages, int timeout);

    @Test
    public void testSequential() {
        Pipeline pipeline = create(List.of(
                TestStage.pro(List.of("a")),
                TestStage.req(List.of("a"))
        ));

        PipelineContext ctx = pipeline.execute(PipelineContext.newInstance());
        assertTrue(ctx.keys().contains("a"));
    }

    @Test
    public void testParallel() {
        Pipeline pipeline = create(List.of(
                TestStage.pro(List.of("a")),
                new TestStage(List.of("a"), List.of("b")),
                new TestStage(List.of("a"), List.of("c"))
        ));

        PipelineContext ctx = pipeline.execute(PipelineContext.newInstance());
        assertTrue(ctx.keys().contains("a"));
        assertTrue(ctx.keys().contains("b"));
        assertTrue(ctx.keys().contains("c"));
    }

    @Test
    public void testTimeout() {
        Pipeline pipeline = create(List.of(
                new Stage() {
                    @Override
                    public Collection<String> required() {
                        return List.of();
                    }

                    @Override
                    public Collection<String> provides() {
                        return List.of();
                    }

                    @Override
                    public PipelineContext execute(PipelineContext ctx) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException("Something bad happened", e);
                        }
                        return ctx;
                    }
                }
        ), 10);

        assertThrows(ThreadTimeoutExceededException.class, () -> pipeline.execute(PipelineContext.newInstance()));
    }
}
