package org.dorkmaster.flow;

import java.util.Map;

public interface ExpressionBuilder {
    Expression build(FlowBuilder builder, Map<String, Object> data);
}
