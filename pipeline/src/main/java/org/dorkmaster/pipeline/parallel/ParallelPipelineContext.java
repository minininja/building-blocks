package org.dorkmaster.pipeline.parallel;

import org.dorkmaster.pipeline.PipelineContext;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParallelPipelineContext implements PipelineContext {
    private Map<String, Object> context = new ConcurrentHashMap<>();

    public ParallelPipelineContext() {
    }

    public ParallelPipelineContext(PipelineContext ctx) {
        merge(ctx);
    }

    @Override
    public Collection<String> keys() {
        return context.keySet();
    }

    @Override
    public PipelineContext set(String key, Object value) {
        context.put(key, value);
        return this;
    }

    @Override
    public Object get(String key) {
        return context.get(key);
    }

    /**
     * Merges keys from the passed into context into itself, replacing any keys that already exist with new values
     * @param context
     * @return
     */
    public ParallelPipelineContext merge(PipelineContext context) {
        for (String key : context.keys()) {
            this.set(key, context.get(key));
        }
        return this;
    }
}
