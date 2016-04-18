package com.xsl.data.collect.kafka;

/**
 * Created by howard on 16/4/15.
 */
public enum KafkaAcksMode {
    ALL("all"), ZERO("0"), ONE("1");

    private String value;

    KafkaAcksMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
