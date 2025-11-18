package org.dorkmaster.flow;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FlowContext {
    private Map<String, Object> ctx = new HashMap<>();

    public Object get(String key) {
        return ctx.get(key);
    }

    public FlowContext set(String key, Object value) {
        ctx.put(key, value);
        return this;
    }

    public Collection<Map.Entry<String, Object>> entries(){
        return ctx.entrySet();
    }
}
