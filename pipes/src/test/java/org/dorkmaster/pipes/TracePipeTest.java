package org.dorkmaster.pipes;

import org.dorkmaster.pipes.util.Helpers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TracePipeTest extends Helpers {
    @Test
    public void trace() {
        TracePipe<Integer, Integer> pipe = new TracePipe<>("mesg");
        pipe.setNext(addOne);

        int result = pipe.process(1);

        assertEquals(2, result);
    }
}
