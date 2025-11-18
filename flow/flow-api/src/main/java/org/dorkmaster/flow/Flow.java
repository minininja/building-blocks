package org.dorkmaster.flow;

public class Flow implements Action {
    private Expression expression;
    private Action action;

    public Flow setExpression(Expression expression) {
        this.expression = expression;
        return this;
    }

    public Flow setAction(Action action) {
        this.action = action;
        return this;
    }

    public Expression getExpression(){
        return expression;
    }

    public Action getAction(){
        return action;
    }

    @Override
    public FlowContext execute(FlowContext ctx) {
        if (expression == null || expression.isValid(ctx)) {
            return action.execute(ctx);
        }
        return ctx;
    }
}
