package org.dorkmaster.pipes.util;

import org.dorkmaster.pipes.Pipe;

public class Helpers {
    protected final Pipe<Integer, Integer> delay = new Pipe<Integer, Integer>() {
        @Override
        public Integer process(Integer input) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return input;
        }
    };

    protected final Pipe<Integer, Integer> addOne = new Pipe<Integer, Integer>() {
        @Override
        public Integer process(Integer input) {
            return input + 1;
        }
    };

    protected final Pipe<Integer, Integer> doubler = new Pipe<Integer, Integer>() {
        @Override
        public Integer process(Integer input) {
            return input * 2;
        }
    };

    protected final Pipe<Integer, Integer> nullResult =  new Pipe<Integer, Integer>() {
        @Override
        public Integer process(Integer input) {
            return null;
        }
    };
}
