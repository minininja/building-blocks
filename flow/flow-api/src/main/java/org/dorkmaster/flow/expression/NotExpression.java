package org.dorkmaster.flow.expression;

import org.dorkmaster.flow.Expression;
import org.dorkmaster.flow.FlowContext;

public class NotExpression implements Expression {
    protected Expression expression;

    public NotExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public boolean isValid(FlowContext ctx) {
        return !expression.isValid(ctx);
    }
}
