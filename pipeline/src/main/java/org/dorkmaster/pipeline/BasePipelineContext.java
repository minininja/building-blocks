package org.dorkmaster.pipeline;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BasePipelineContext implements PipelineContext {
    private Map<String, Object> context = new HashMap<>();

    @Override
    public Collection<String> keys() {
        return context.keySet();
    }

    @Override
    public BasePipelineContext set(String key, Object value) {
        context.put(key, value);
        return this;
    }

    @Override
    public Object get(String key) {
        return context.get(key);
    }
}
