package org.dorkmaster.pipes;

public abstract class AbstractChainedPipe<I,O> implements Pipe<I,O> {
    private Pipe<I,O> next;

    public AbstractChainedPipe<I, O> setNext(Pipe<I,O> next) {
        this.next = next;
        return this;
    }

    protected O next(I input) {
        return next.process(input);
    }
}
