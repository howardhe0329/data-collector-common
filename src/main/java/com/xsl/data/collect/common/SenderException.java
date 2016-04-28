package com.xsl.data.collect.common;

/**
 * Created by howard on 16/4/27.
 */
public class SenderException extends Exception {

    public SenderException() {
        super();
    }

    public SenderException(String message) {
        super(message);
    }

    public SenderException(String message, Throwable t) {
        super(message, t);
    }

    public SenderException(Throwable t) {
        super(t);
    }
}
