package org.dorkmaster.examples.pipeline.virtual;

import feign.Feign;
import feign.FeignException;
import feign.Param;
import feign.RequestLine;
import org.dorkmaster.pipeline.Pipeline;
import org.dorkmaster.pipeline.PipelineContext;
import org.dorkmaster.pipeline.PipelineFactory;
import org.dorkmaster.pipeline.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpPipelineExamples {
    static Collection<Stage> httpRequestStages = List.of(
            new Stage() {
                static final String P = "github";
                static final GitHub github = Feign.builder()
                        .target(GitHub.class, "http://api.github.com");

                @Override
                public Collection<String> required() {
                    return Collections.emptyList();
                }

                @Override
                public Collection<String> provides() {
                    return List.of(P);
                }

                @Override
                public PipelineContext execute(PipelineContext ctx) {
                    try {
                        ctx.set(P, github.contributors("OpenFeign", "feign"));
                    } catch (FeignException e){
                        ctx.set(P, e.status());
                    }
                    return ctx;
                }
            },
            new Stage() {
                static final String P = "objects";
                static final RestfulApiDev restfulApiDev = Feign.builder()
                        .target(RestfulApiDev.class, "https://restful-api.dev");

                @Override
                public Collection<String> required() {
                    return List.of();
                }

                @Override
                public Collection<String> provides() {
                    return List.of(P);
                }

                @Override
                public PipelineContext execute(PipelineContext ctx) {
                    try {
                        ctx.set(P, restfulApiDev.objects());
                    } catch (FeignException e){
                        ctx.set(P, e.status());
                    }
                    return ctx;
                }
            }
    );

    @BeforeAll
    public static void warmup() {
        Pipeline pipeline = PipelineFactory.createStandard(httpRequestStages);
        PipelineContext ctx = PipelineContext.newInstance();

        ctx = pipeline.execute(ctx);
        assertEquals(ctx.keys().size(), 2);
    }

    @Test
    public void httpRunNoThreads() {
        Pipeline pipeline = PipelineFactory.createStandard(httpRequestStages);
        PipelineContext ctx = PipelineContext.newInstance();

        Timer t = new Timer();
        ctx = pipeline.execute(ctx);
        long runtime = t.delta();

        System.out.println("http runtime on no threads:" + runtime);
        assertEquals(ctx.keys().size(), 2);
    }

    @Test
    public void httpRunVirtualThreads() {
        Pipeline pipeline = PipelineFactory.createVirtual(httpRequestStages);
        PipelineContext ctx = PipelineContext.newInstance();

        Timer t = new Timer();
        ctx = pipeline.execute(ctx);
        long runtime = t.delta();

        System.out.println("http runtime on virtual threads:" + runtime);
        assertEquals(ctx.keys().size(), 2);
    }

    @Test
    public void httpRunNativeThreads() {
        Pipeline pipeline = PipelineFactory.createNative(httpRequestStages);
        PipelineContext ctx = PipelineContext.newInstance();

        Timer t = new Timer();
        ctx = pipeline.execute(ctx);
        long runtime = t.delta();

        System.out.println("http runtime on native threads:" + runtime);
        assertEquals(ctx.keys().size(), 2);
    }

    interface GitHub {
        @RequestLine("GET /repos/{owner}/{repo}/contributors")
        Collection<Object> contributors(@Param("owner") String owner, @Param("repo") String repo);
    }

    interface RestfulApiDev {
        // https://restful-api.dev
        @RequestLine("GET api.restful-api.dev/objects")
        Collection<Object> objects();
    }

    class Timer {
        long start = System.currentTimeMillis();

        long delta() {
            return System.currentTimeMillis() - start;
        }
    }
}
