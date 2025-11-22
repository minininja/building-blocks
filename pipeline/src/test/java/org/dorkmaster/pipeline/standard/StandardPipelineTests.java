package org.dorkmaster.pipeline.standard;

import org.dorkmaster.pipeline.*;
import org.dorkmaster.pipeline.exception.PipelineExecutionException;
import org.dorkmaster.pipeline.exception.UnresolvedDependenciesException;
import org.dorkmaster.pipeline.util.TestStage;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StandardPipelineTests {

    @Test
    public void testHappyStandard() {
        Pipeline pipeline = PipelineFactory.createStandard(List.of(
                new Stage() {
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
                }
        ));

        PipelineContext ctx = pipeline.execute(PipelineContext.newInstance().set("a", "a"));
        assertEquals(ctx.get("b"), "b");
    }

    @Test
    public void testMissingRequired() {
        Pipeline pipeline = PipelineFactory.createStandard(List.of(
                new TestStage(List.of("a"),List.of("b"))
        ));

        assertThrows(UnresolvedDependenciesException.class, () -> pipeline.execute(PipelineContext.newInstance()));
    }

    @Test
    public void testMissingProvided() {
        Pipeline pipeline = PipelineFactory.createStandard(List.of(
                new TestStage(Collections.emptyList(), List.of("b"), Collections.emptyList()))
        );

        assertThrows(PipelineExecutionException.class, () -> pipeline.execute(PipelineContext.newInstance()));
    }
}
