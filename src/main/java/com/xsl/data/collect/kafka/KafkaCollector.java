package com.xsl.data.collect.kafka;

import com.xsl.data.collect.Collector;
import com.xsl.data.collect.common.CommonConfiguration;
import com.xsl.data.collect.common.DebugMode;
import com.xsl.data.collect.core.Sender;
import com.xsl.data.collect.event.Event;
import com.xsl.data.collect.util.ConfigurationProvider;

import java.util.Properties;

/**
 * Created by howard on 16/4/14.
 */
public class KafkaCollector implements Collector {

    private Sender sender;

    private KafkaCollector(Sender sender) {
        this.sender = sender;
    }

    @Override
    public void collect(Event event) {
        sender.send(event);
    }

    @Override
    public void collect(String name, Event event) {
        sender.send(name, event);
    }

    @Override
    public void shutdown() {
        sender.stop();
    }

    public static class Builder {

        private Boolean debug;
        private Sender sender;
        private KafkaConf kafkaConf;

        public Builder isDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder conf(KafkaConf kafkaConf) {
            this.kafkaConf = kafkaConf;
            return this;
        }

        public KafkaCollector build() {
            sender = new KafkaSender();
            ConfigurationProvider provider = new ConfigurationProvider();
            Properties properties = provider.loads();
            if (debug != null) {
                properties.setProperty(CommonConfiguration.DEBUG_MODE, debug ? DebugMode.ON.name() : DebugMode.OFF.name());
            }
            if(kafkaConf != null) {
                String topic = kafkaConf.getTopic();
                if(topic != null && !topic.isEmpty()) {
                    properties.setProperty(KafkaSenderConfiguration.TOPIC, topic);
                }
                String brokerList = kafkaConf.getBrokerList();
                if(brokerList != null && !brokerList.isEmpty()) {
                    properties.setProperty(KafkaSenderConfiguration.BROKER_LIST, brokerList);
                }
                KafkaAcksMode kafkaAcksMode = kafkaConf.getAcks();
                if(kafkaAcksMode != null) {
                    properties.setProperty(KafkaSenderConfiguration.REQUIRED_ACKS, kafkaAcksMode.getValue());
                }
                DeliveryMode deliveryMode = kafkaConf.getDeliveryMode();
                if(deliveryMode != null) {
                    properties.setProperty(KafkaSenderConfiguration.DELIVERY_STRATEGY, deliveryMode.name());
                }
                Integer timeout = kafkaConf.getTimeout();
                if(timeout != null) {
                    properties.setProperty(KafkaSenderConfiguration.DELIVERY_TIMEOUT, String.valueOf(timeout));
                }
            }
            sender.configure(properties);
            sender.start();
            return new KafkaCollector(sender);
        }
    }
}
