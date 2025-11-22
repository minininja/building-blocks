package org.dorkmaster.pipeline;

import org.dorkmaster.pipeline.serial.DependencyResolver;
import org.dorkmaster.pipeline.util.TestStage;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DependencyResolverTest {

    @Test
    public void testSimple() {
        PipelineContext ctx = PipelineContext.newInstance();
        Collection<Collection<Stage>> result = new DependencyResolver().orderStages(ctx, List.of(
                TestStage.nada(),
                TestStage.nada()
        )).getResolvedStages();

        assertEquals(result.size(), 1);
        assertEquals(result.iterator().next().size(), 2);
    }

    @Test
    public void testOnlyProviders() {
        PipelineContext ctx = PipelineContext.newInstance();
        Collection<Collection<Stage>> result = new DependencyResolver().orderStages(ctx, List.of(
                TestStage.pro(List.of("a")),
                TestStage.pro(List.of("b"))
        )).getResolvedStages();

        assertEquals(result.size(), 1);
        assertEquals(result.iterator().next().size(), 2);
    }

    @Test
    public void testDependenciesInOrder() {
        PipelineContext ctx = PipelineContext.newInstance();
        Collection<Collection<Stage>> result = new DependencyResolver().orderStages(ctx, List.of(
                new TestStage(Collections.emptySet(), Set.of("a", "b")),
                TestStage.req(List.of("a")),
                TestStage.req(List.of("b"))
        )).getResolvedStages();

        assertEquals(result.size(), 2);
        Iterator<Collection<Stage>> it = result.iterator();
        assertEquals(it.next().size(), 1);
        assertEquals(it.next().size(), 2);
    }

    @Test
    public void testReversedDependencies() {
        PipelineContext ctx = PipelineContext.newInstance();
        Collection<Collection<Stage>> result = new DependencyResolver().orderStages(ctx, List.of(
                TestStage.req(List.of("a")),
                TestStage.req(List.of("b")),
                new TestStage(Collections.emptySet(), Set.of("a", "b"))
        )).getResolvedStages();

        assertEquals(result.size(), 2);
        Iterator<Collection<Stage>> it = result.iterator();
        assertEquals(it.next().size(), 1);
        assertEquals(it.next().size(), 2);
    }

    @Test
    public void testContextProvidedDependencies() {
        PipelineContext ctx = PipelineContext.newInstance().set("z","a");
        Collection<Collection<Stage>> result = new DependencyResolver().orderStages(ctx, List.of(
                TestStage.req(List.of("a")),
                TestStage.req(List.of("z", "b")),
                new TestStage(Collections.emptySet(), Set.of("a", "b"))
        )).getResolvedStages();

        assertEquals(result.size(), 2);
        Iterator<Collection<Stage>> it = result.iterator();
        assertEquals(it.next().size(), 1);
        assertEquals(it.next().size(), 2);
    }

    @Test
    public void testUnresolvable() {
        PipelineContext ctx = PipelineContext.newInstance();
        assertThrows(
                RuntimeException.class,
                () -> new DependencyResolver().orderStages(ctx, List.of(
                        TestStage.req(List.of("a")),
                        TestStage.req(List.of("b", "c")),
                        new TestStage(Collections.emptySet(), Set.of("a", "b"))
                ))
        );
    }
}
