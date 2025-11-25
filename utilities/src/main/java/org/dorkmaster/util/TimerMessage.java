package org.dorkmaster.util;

public class TimerMessage extends Timer {
    public void delta(String mesg){
        System.out.println(mesg + " " + super.delta() + "ms");
    }
}
