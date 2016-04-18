package com.xsl.data.collect.kafka;

/**
 * Created by howard on 16/4/15.
 */
public enum DeliveryMode {
    SYNC, ASYNC;

    public String toClassName() {
        if(this.equals(SYNC)) {
            return "com.xsl.data.collect.kafka.BlockingDeliveryStrategy";
        } else if(this.equals(ASYNC)) {
            return "com.xsl.data.collect.kafka.AsynchronousDeliveryStrategy";
        } else {
            return "com.xsl.data.collect.kafka.BlockingDeliveryStrategy";
        }
    }
}
