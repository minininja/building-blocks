package org.dorkmaster.pipes;

import org.dorkmaster.pipes.util.Helpers;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FanOutPipeTest extends Helpers {
    @Test
    public void testFanout() throws InterruptedException {
        FanoutPipe<Integer, Integer> fanout = new FanoutPipe<>(1, 1);
        fanout.setNext(addOne);

        // this returns null so don't bother with capturing the result
        fanout.process(1);

        fanout.shutdown();
        fanout.awaitTermination(1, TimeUnit.SECONDS);
        Collection<Integer> threadResults = fanout.results();
        assertEquals(1, threadResults.size());
        assertEquals(2, threadResults.iterator().next());
    }

    @Test
    public void testAwait() throws InterruptedException {
        FanoutPipe<Integer, Integer> fanout = new FanoutPipe<>(1, 1);
        fanout.setNext(delay);

        // this returns null so don't bother with capturing the result
        fanout.process(1);

        fanout.shutdown();
        // await too short a time
        assertFalse(fanout.awaitTermination(10, TimeUnit.MILLISECONDS));
        Collection<Integer> threadResults = fanout.results();
        assertTrue(threadResults.isEmpty());

        // now wait for it to close
        assertTrue(fanout.awaitTermination(100, TimeUnit.MILLISECONDS));
        threadResults = fanout.results();
        assertEquals(1, threadResults.size());
        assertEquals(1, threadResults.iterator().next());
    }
}
