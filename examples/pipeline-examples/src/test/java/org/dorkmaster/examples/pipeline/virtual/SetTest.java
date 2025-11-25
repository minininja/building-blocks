package org.dorkmaster.examples.pipeline.virtual;

import org.dorkmaster.util.TimerMessage;
import org.junit.jupiter.api.Test;

import java.util.*;

public class SetTest {

    @Test
    public void test() {
        Set<SetObject> set = new HashSet<>();
        Collection<SetObject> toRemove = new LinkedList<>();

        for (int i = 0; i < 1027*768*10; i++){
            SetObject obj = new SetObject(i);

            if (i != 0){
                toRemove.add(obj);
            }
            set.add(obj);
        }

        String mesg = "Removed " +toRemove.size() + " elements in";
        TimerMessage tm = new TimerMessage();
        set.removeAll(toRemove);
        tm.delta(mesg);
    }

    class SetObject {
        int id;
        int xRes = 1024;
        int yRes = 768;

        double pMin = -2.0;
        double pMax = 2.0;
        double qMin = -2.0;
        double qMax = 2.0;

        int i;
        double max = 4.0;

        double cxMin;
        double cxMax;
        double cyMin;
        double cyMax;

        public SetObject(int id) {
            this.id = id;

            Random r = new Random();
            i = r.nextInt();
            max = r.nextDouble();
            cxMin = r.nextDouble();
            cxMax = r.nextDouble();
            cyMin = r.nextDouble();
            cyMax = r.nextDouble();
        }
    }
}
