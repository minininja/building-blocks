package org.dorkmaster.pipes;

import org.dorkmaster.pipes.util.Helpers;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StrategyPipeTest extends Helpers {
    Function<Integer, Boolean> left = new Function<> () {
        @Override
        public Boolean apply(Integer integer) {
            return true;
        }
    };

    Function<Integer, Boolean> right = new Function<> () {
        @Override
        public Boolean apply(Integer integer) {
            return false;
        }
    };

    @Test
    public void firstExecutes() {
        StrategyPipe<Integer,Integer> pipe = new StrategyPipe<>(left);
        pipe.setLeft(addOne).setRight(doubler);

        int result = pipe.process(1);
        assertEquals(2, result);
    }

    @Test
    public void secondExecutes() {
        StrategyPipe<Integer,Integer> pipe = new StrategyPipe<>(right);
        pipe.setLeft(addOne).setRight(doubler);

        int result = pipe.process(2);
        assertEquals(4, result);
    }
}
