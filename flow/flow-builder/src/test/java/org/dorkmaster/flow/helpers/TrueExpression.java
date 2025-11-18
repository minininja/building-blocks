package org.dorkmaster.flow.helpers;

import org.dorkmaster.flow.Expression;
import org.dorkmaster.flow.FlowContext;

public class TrueExpression implements Expression {
    @Override
    public boolean isValid(FlowContext ctx) {
        return true;
    }
}
