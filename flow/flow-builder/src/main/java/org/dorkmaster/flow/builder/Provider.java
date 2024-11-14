package org.dorkmaster.flow.builder;

import org.dorkmaster.flow.Action;
import org.dorkmaster.flow.Expression;
import org.dorkmaster.flow.Flow;
import org.dorkmaster.flow.FlowContext;

public interface Provider {
    String namespace();
    Flow flowInstance(String name);
    Expression expressionInstance(String name);
    Action actionInstance(String name);

    static Expression nullExpression() {
        return new Expression() {
            @Override
            public boolean isValid(FlowContext ctx) {
                return true;
            }
        };
    }
}
