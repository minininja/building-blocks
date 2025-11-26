package org.dorkmaster.util;

/**
 * Just a generic timer class.
 */
public class Timer {
    protected long start = System.currentTimeMillis();

    public long delta() {
        return System.currentTimeMillis() - start;
    }
}
