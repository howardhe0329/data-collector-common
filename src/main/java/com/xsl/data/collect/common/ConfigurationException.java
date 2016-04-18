package com.xsl.data.collect.common;

/**
 * Created by howard on 16/4/13.
 */
public class ConfigurationException extends DataCollectException {

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(Throwable cause) {
        super(cause);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
