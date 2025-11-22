package org.dorkmaster.pipeline.parallel;

import org.dorkmaster.pipeline.PipelineContext;
import org.dorkmaster.pipeline.exception.PipelineExecutionException;
import org.dorkmaster.pipeline.Stage;
import org.dorkmaster.pipeline.exception.ThreadTimeoutExceededException;
import org.dorkmaster.pipeline.serial.StandardPipeline;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.*;

public class VirtualThreadedPipeline extends StandardPipeline {
    protected int timeout = 5000;

    public VirtualThreadedPipeline(Collection<Stage> stages) {
        super(stages);
    }

    public VirtualThreadedPipeline(Collection<Stage> stages, int timeout) {
        super(stages);
        this.timeout = timeout;
    }

    protected PipelineContext doExecute(PipelineContext incomingContext, Collection<Collection<Stage>> bucketedStages) {
        ParallelPipelineContext ctx = new ParallelPipelineContext(incomingContext);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (Collection<Stage> step : bucketedStages) {
                Collection<Future<PipelineContext>> tasks = new LinkedList<>();
                for (Stage stage : step) {
                    tasks.clear();
                    tasks.add(
                            (Future<PipelineContext>) executor.submit(() -> {
                                ctx.merge(stage.execute(ctx));
                                if (!postVerify(stage, ctx)) {
                                    throw new PipelineExecutionException("Post execution product missing", ctx);
                                }
                            })
                    );
                }
                for (Future<PipelineContext> future : tasks) {
                    future.get(timeout, TimeUnit.MILLISECONDS);
                }
            }
            executor.shutdown();
        } catch (ExecutionException|InterruptedException|TimeoutException e) {
            throw new ThreadTimeoutExceededException("Timeout exceeded", timeout);
        }

        return ctx;
    }
}
