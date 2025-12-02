package org.dorkmaster.pipes;

import org.dorkmaster.pipes.util.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SplitPipeTest extends Helpers {
    @Test
    public void testSplitSingle() {
        SplitPipe<Integer, Integer> pipe = new SplitPipe<Integer, Integer>();
        pipe.addSplit(addOne);

        Integer result = pipe.process(1);

        assertEquals(2, result);
    }

    @Test
    public void testSplitNull() {
        SplitPipe<Integer, Integer> pipe = new SplitPipe<>();
        pipe.addSplit(addOne).addSplit(nullResult);

        Integer result = pipe.process(1);

        assertNull(result);
        assertEquals(1, pipe.getResults().size());
    }

    @Test
    public void testSplitOne() {
        SplitPipe<Integer, Integer> pipe = new SplitPipe<>();
        pipe.addSplit(nullResult).addSplit(addOne);

        Integer result = pipe.process(1);

        assertEquals(2, result);
        assertEquals(1, pipe.getResults().size());
    }
}
