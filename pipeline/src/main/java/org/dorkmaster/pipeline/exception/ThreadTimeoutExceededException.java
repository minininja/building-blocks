package org.dorkmaster.pipeline.exception;

public class ThreadTimeoutExceededException extends PipelineException {
    protected int timeout;

    public ThreadTimeoutExceededException(String message, int timeout) {
        super(message);
        this.timeout = timeout;
    }

    public int getTimeout() {
        return timeout;
    }
}
