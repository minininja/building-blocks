package org.dorkmaster.pipeline.parallel;

import org.dorkmaster.pipeline.PipelineContext;
import org.dorkmaster.pipeline.exception.PipelineExecutionException;
import org.dorkmaster.pipeline.Stage;
import org.dorkmaster.pipeline.exception.ThreadTimeoutExceededException;
import org.dorkmaster.pipeline.serial.StandardPipeline;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.*;

public class NativeTheadedPipeline extends StandardPipeline {
    protected static ForkJoinPool forkJoinPool;
    protected int timeout = 5000; // 5 seconds

    static {
        ForkJoinPool.ForkJoinWorkerThreadFactory factory = new ForkJoinPool.ForkJoinWorkerThreadFactory() {
            @Override
            public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
                ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
                worker.setName("NativeThreadedPipeline-" + worker.getPoolIndex());
                return worker;
            }
        };

        forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() - 1);

        // cleanup when we're shutting down
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            forkJoinPool.shutdownNow();
        }));
    }

    public NativeTheadedPipeline(Collection<Stage> stages) {
        super(stages);
    }

    public NativeTheadedPipeline(Collection<Stage> stages, int timeout) {
        super(stages);
        this.timeout = timeout;
    }

    protected PipelineContext doExecute(PipelineContext incomingCtx, Collection<Collection<Stage>> bucketedStages) {
        final ParallelPipelineContext ctx = new ParallelPipelineContext(incomingCtx);

        ForkJoinTask<PipelineContext> pending;
        Collection<ForkJoinTask<PipelineContext>> tasks = new LinkedList<>();
        for (Collection<Stage> step : bucketedStages) {
            tasks.clear();
            for (Stage stage : step) {
                pending =
                        (ForkJoinTask<PipelineContext>) forkJoinPool.submit(new Runnable() {
                            public void run() {
                                ctx.merge(stage.execute(ctx));
                                if (!postVerify(stage, ctx)) {
                                    throw new PipelineExecutionException("Post execution product missing", ctx);
                                }
                            }
                        });
                tasks.add(pending);
            }
            for(ForkJoinTask<PipelineContext> join : tasks){
                try {
                    join.get(timeout, TimeUnit.MILLISECONDS);
                } catch (InterruptedException|ExecutionException|TimeoutException e) {
                    throw new ThreadTimeoutExceededException("Join timed out", timeout);
                }
            }
        }

        return ctx;
    }
}
