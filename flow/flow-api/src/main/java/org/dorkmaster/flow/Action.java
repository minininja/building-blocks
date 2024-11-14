package org.dorkmaster.flow;

public interface Action {
    FlowContext execute(FlowContext ctx);
}
