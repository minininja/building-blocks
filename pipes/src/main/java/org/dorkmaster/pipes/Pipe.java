package org.dorkmaster.pipes;

public interface Pipe <I,O> {
    O process(I input);
}
