package com.xsl.data.collect.kafka;

/**
 * Created by howard on 16/4/13.
 */
public class KafkaSenderConfiguration {

    public static final String TOPIC = "kafka.topic";
    public static final String TOPICS = "kafka.topics";
    public static final String BROKER_LIST = "kafka.broker.list";
    public static final String REQUIRED_ACKS= "kafka.acks";
    public static final String DELIVERY_STRATEGY = "delivery.mode";
    public static final String DELIVERY_TIMEOUT = "delivery.timeout.ms";

    public static final String DEFAULT_DELIVERY_TIMEOUT = "500";
    public static final KafkaAcksMode DEFAULT_REQUIRED_ACKS = KafkaAcksMode.ONE;
    public static final DeliveryMode DEFAULT_DELIVERY_STRATEGY = DeliveryMode.SYNC;



}
