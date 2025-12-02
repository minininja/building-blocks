package org.dorkmaster.pipes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TracePipe<I,O> extends AbstractChainedPipe<I,O> {
    private static final Logger logger = LoggerFactory.getLogger(TracePipe.class);
    protected String mesg;

    public TracePipe(String mesg) {
        this.mesg = mesg;
    }

    @Override
    public O process(I input) {
        logger.trace("{} with {}", mesg, input);
        return next(input);
    }
}
