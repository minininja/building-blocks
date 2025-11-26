package org.dorkmaster.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimerMessage extends Timer {
    private Logger logger = LoggerFactory.getLogger(TimerMessage.class);

    public void delta(String mesg){
        logger.trace("{}  {}ms", mesg, super.delta());
    }
}
