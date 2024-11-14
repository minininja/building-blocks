package org.dorkmaster.pipeline;

import java.util.Collection;

public interface PipelineContext {
    Collection<String> keys();
    PipelineContext set(String key, Object value);
    Object get(String key);

    static PipelineContext newInstance() {
        return new BasePipelineContext();
    }
}
