package org.dorkmaster.flow;

import java.util.Map;

public interface ActionBuilder {
    Action build(FlowBuilder builder, Map<String, Object> data);
}
