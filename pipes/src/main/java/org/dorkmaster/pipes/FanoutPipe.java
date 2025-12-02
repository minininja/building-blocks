package org.dorkmaster.pipes;

import org.dorkmaster.pipes.exception.PipeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class FanoutPipe<I, O> extends AbstractChainedPipe<I, O> {
    protected final Logger logger = LoggerFactory.getLogger(FanoutPipe.class);
    protected List<Future<O>> futures = new LinkedList<>();
    protected ThreadPoolExecutor executor;
    protected BlockingQueue<Runnable> queue;

    public FanoutPipe(int maxThreads, int maxQueue) {
//        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(maxQueue);
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
        executor = new ThreadPoolExecutor(
                maxThreads,
                maxThreads,
                60,
                TimeUnit.MILLISECONDS,
                queue
        );
    }

    public O process(I input) {
        futures.add(
                executor.submit(() -> {
                    logger.info("Thread {} running {}", Thread.currentThread().getName(), input);
                    return next(input);
                })
        );
        return null;
    }

    public void shutdown() {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }

    public boolean awaitTermination(int timeout, TimeUnit unit) throws InterruptedException {
        return executor.awaitTermination(timeout, unit);
    }

    public Collection<O> results() {
        try {
            if (!executor.isTerminated()) {
                logger.warn("Executor is not shutdown");
                return Collections.emptyList();
            }

            // gather the results to return
            Collection<O> results = new ArrayList<>(futures.size());
            for (Future<O> f : futures) {
                results.add(f.get());
            }

            return results;
        } catch (InterruptedException | ExecutionException e) {
            throw new PipeException("Unexpected error since executor service should be down", e);
        }
    }
}
