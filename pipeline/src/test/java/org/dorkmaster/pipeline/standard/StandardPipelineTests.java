package org.dorkmaster.pipeline.standard;

import org.dorkmaster.pipeline.PipelineContext;
import org.dorkmaster.pipeline.PipelineException;
import org.dorkmaster.pipeline.Stage;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StandardPipelineTests {

    @Test
    public void testHappyStandard() {
        Pipeline pipeline = new Pipeline()
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
                });

        PipelineContext ctx = pipeline.execute(PipelineContext.newInstance().set("a", "a"));
        assertEquals(ctx.get("b"), "b");
    }


    @Test
    public void testMissingRequired() {
        Pipeline pipeline = new Pipeline()
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
                        throw new NullPointerException("PipelineContext't get here");
                    }
                });

        try {
            PipelineContext result = pipeline.execute(PipelineContext.newInstance());
            assertTrue(false, "Should not get here");
        }
        catch(PipelineException e) {
            assertNotNull(e.getCtx());
        }
    }

    @Test
    public void testMissingProvided() {
        Pipeline pipeline = new Pipeline()
                .addStage(new Stage() {
                    @Override
                    public Collection<String> required() {
                        return Collections.emptyList();
                    }

                    @Override
                    public Collection<String> provides() {
                        return List.of("b");
                    }

                    @Override
                    public PipelineContext execute(PipelineContext ctx) {
                        return ctx;
                    }
                });

        try {
            PipelineContext result = pipeline.execute(PipelineContext.newInstance().set("a", "a"));
            assertTrue(false, "Should not get here");
        }
        catch(PipelineException e) {
            assertNotNull(e.getCtx());
        }
    }
}
