package org.dorkmaster.flow.expression;

import org.dorkmaster.flow.Expression;

import java.util.Collection;

public abstract class CompositeExpression implements Expression {
    protected Collection<Expression> expressions;

    public CompositeExpression(Collection<Expression> expressions) {
        this.expressions = expressions;
    }
}
