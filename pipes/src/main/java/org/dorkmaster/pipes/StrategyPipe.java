package org.dorkmaster.pipes;

import java.util.function.Function;

public class StrategyPipe<I, O> implements Pipe<I, O> {
    protected Function<I, Boolean> decider;
    protected Pipe<I, O> left, right;

    public StrategyPipe(Function<I, Boolean> decider) {
        this.decider = decider;
    }

    public StrategyPipe<I, O> setLeft(Pipe<I, O> left) {
        this.left = left;
        return this;
    }

    public StrategyPipe<I, O> setRight(Pipe<I, O> right) {
        this.right = right;
        return this;
    }

    @Override
    public O process(I input) {
        if (decider.apply(input)) {
            return left.process(input);
        }
        else {
            return right.process(input);
        }
    }
}
