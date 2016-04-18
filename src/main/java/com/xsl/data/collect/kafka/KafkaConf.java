package com.xsl.data.collect.kafka;

import java.util.Properties;

/**
 * Created by howard on 16/4/15.
 */
public class KafkaConf {

    private String brokerList;
    private String topic;
    private DeliveryMode deliveryMode;
    private Integer timeout;
    private KafkaAcksMode acks;

    public KafkaConf brokerList(String brokerList) {
        this.brokerList = brokerList;
        return this;
    }

    public KafkaConf topic(String topic) {
        this.topic = topic;
        return this;
    }

    public KafkaConf deliveryMode(DeliveryMode deliveryMode) {
        this.deliveryMode = deliveryMode;
        return this;
    }

    public KafkaConf timeout(Integer timeout) {
        this.timeout = timeout;
        return this;
    }

    public KafkaConf acks(KafkaAcksMode kafkaAcksMode) {
        this.acks = kafkaAcksMode;
        return this;
    }

    public Properties toProperties() {
        Properties properties = new Properties();
        properties.setProperty(KafkaSenderConfiguration.BROKER_LIST, brokerList);
        properties.setProperty(KafkaSenderConfiguration.TOPIC,topic);
        if(deliveryMode == null) {
            deliveryMode = KafkaSenderConfiguration.DEFAULT_DELIVERY_STRATEGY;
        }
        properties.setProperty(KafkaSenderConfiguration.DELIVERY_STRATEGY, deliveryMode.toClassName());
        if(timeout == null) {
            timeout = Integer.valueOf(KafkaSenderConfiguration.DEFAULT_DELIVERY_TIMEOUT);
        }
        properties.setProperty(KafkaSenderConfiguration.DELIVERY_TIMEOUT, String.valueOf(timeout));
        if(acks == null)
            acks = KafkaSenderConfiguration.DEFAULT_REQUIRED_ACKS;
        properties.setProperty(KafkaSenderConfiguration.REQUIRED_ACKS, acks.getValue());
        return properties;
    }

    public KafkaConf() {
    }

    public KafkaConf(String brokerList, String topic) {
        this.brokerList = brokerList;
        this.topic = topic;
    }

    public KafkaConf(String brokerList, String topic, DeliveryMode deliveryMode, Integer timeout) {
        this.brokerList = brokerList;
        this.topic = topic;
        this.deliveryMode = deliveryMode;
        this.timeout = timeout;
    }

    public String getBrokerList() {
        return brokerList;
    }

    public void setBrokerList(String brokerList) {
        this.brokerList = brokerList;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public DeliveryMode getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(DeliveryMode deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public KafkaAcksMode getAcks() {
        return acks;
    }

    public void setAcks(KafkaAcksMode acks) {
        this.acks = acks;
    }

    @Override
    public String toString() {
        return "KafkaConf{" +
                "brokerList='" + brokerList + '\'' +
                ", topic='" + topic + '\'' +
                ", deliveryMode=" + deliveryMode +
                ", timeout=" + timeout +
                ", acks=" + acks +
                '}';
    }
}
