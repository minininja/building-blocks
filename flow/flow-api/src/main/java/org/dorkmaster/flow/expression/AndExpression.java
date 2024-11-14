package org.dorkmaster.flow.expression;

import org.dorkmaster.flow.Expression;
import org.dorkmaster.flow.FlowContext;

import java.util.Collection;

public class AndExpression extends CompositeExpression {
    public AndExpression(Collection<Expression> expressions) {
        super(expressions);
    }

    @Override
    public boolean isValid(FlowContext ctx) {
        for (Expression expression : expressions) {
            if (!expression.isValid(ctx)) {
                return false;
            }
        }
        return true;
    }
}
