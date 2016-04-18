package com.xsl.data.collect.common;

/**
 * Created by howard on 16/4/12.
 */
public class DataCollectException extends RuntimeException {

    public DataCollectException(String message) {
        super(message);
    }

    public DataCollectException(Throwable cause) {
        super(cause);
    }

    public DataCollectException(String message, Throwable cause) {
        super(message, cause);
    }
}
