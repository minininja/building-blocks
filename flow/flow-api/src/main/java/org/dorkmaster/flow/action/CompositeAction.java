package org.dorkmaster.flow.action;

import org.dorkmaster.flow.Action;
import org.dorkmaster.flow.FlowContext;

import java.util.Collection;

public class CompositeAction implements Action {
    protected Collection<Action> actions;

    public CompositeAction(Collection<Action> actions) {
        this.actions = actions;
    }

    @Override
    public FlowContext execute(FlowContext ctx) {
        FlowContext curr = ctx;
        for (Action action: actions) {
            curr = action.execute(curr);
        }
        return curr;
    }
}
