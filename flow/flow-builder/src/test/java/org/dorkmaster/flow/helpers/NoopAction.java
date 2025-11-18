package org.dorkmaster.flow.helpers;

import org.dorkmaster.flow.Action;
import org.dorkmaster.flow.FlowContext;

public class NoopAction implements Action {
    @Override
    public FlowContext execute(FlowContext ctx) {
        return ctx;
    }
}
