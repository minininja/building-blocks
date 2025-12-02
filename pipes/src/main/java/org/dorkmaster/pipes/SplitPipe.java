package org.dorkmaster.pipes;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class SplitPipe<I, O> implements Pipe<I, O> {
    private Collection<Pipe<I, O>> splits = new LinkedList<>();
    private Set<O> results = new HashSet<>();

    public SplitPipe<I, O> addSplit(Pipe<I, O> split) {
        splits.add(split);
        return this;
    }

    @Override
    public O process(I input) {
        O result = null;

        for (Pipe<I,O> split : splits) {
            result = split.process(input);
            if (null != result) {
                results.add(result);
            }
        }

        // return the last result
        return result;
    }

    public Collection<O> getResults() {
        return results;
    }
}
