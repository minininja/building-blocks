package org.dorkmaster.util;

public class Timer {
    protected long start = System.currentTimeMillis();

    public long delta() {
        return System.currentTimeMillis() - start;
    }
}
