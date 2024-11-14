package org.dorkmaster.flow;

public interface Expression {
    boolean isValid(FlowContext ctx);
}
