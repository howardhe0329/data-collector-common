package com.xsl.data.collect.kafka;

import com.xsl.data.collect.common.*;
import com.xsl.data.collect.core.AbstractSender;
import com.xsl.data.collect.event.Event;
import com.xsl.data.collect.metrics.Counter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 发送到Kafka
 * Created by howard on 16/4/13.
 */
public class KafkaSender extends AbstractSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSender.class);
    private Producer<String, byte[]> producer;
    private AtomicReference<String> topic = new AtomicReference<>();
    private final Properties kafkaConf = new Properties();
    private DeliveryStrategy deliveryStrategy;
    private DebugMode debugMode;

    public KafkaSender() {
        setName(SendMode.KAFKA.name());
    }

    @Override
    public void start() {
        if(debugMode.equals(DebugMode.ON)) {
            LOGGER.warn("KafkaSender is debug mode");
            super.start();
            return;
        }
        LOGGER.info("Kafka Sender: {} starting... " + getName());
        try {
            producer = new KafkaProducer<>(kafkaConf);
            LOGGER.info("Topic = " + topic.get());
            super.start();
        } catch (Exception e) {
            LOGGER.error("Could not start producer");
            throw new DataCollectException("Unable to create Kafka Connections." +
                    " Check whether Kafka Brokers are up and that the data-collector can connect to it.", e);
        }
        LOGGER.info("Kafka Sender: {} started. " + getName());
    }

    @Override
    public void stop() {
        LOGGER.info("Kafka channel {} stopping...", getName());
        if (producer != null) {
            producer.close();
        }
        super.stop();
        LOGGER.info("Kafka channel {} stopped.", getName());
    }

    @Override
    public void configure(Properties properties) {
        String debugModeName = properties.getProperty(CommonConfiguration.DEBUG_MODE, DebugMode.OFF.name());
        debugMode = DebugMode.valueOf(debugModeName.toUpperCase());
        if(debugMode.equals(DebugMode.ON)) {
            LOGGER.warn("Warning! start debug mode");
            return;
        }
        String topicStr = properties.getProperty(KafkaSenderConfiguration.TOPIC);
        if (topicStr == null || topicStr.isEmpty()) {
            LOGGER.warn("Topic was not specified. Using " + topicStr + " as the topic.");
        } else {
            topic.set(topicStr);
        }
        String brokerList = properties.getProperty(KafkaSenderConfiguration.BROKER_LIST);
        if (brokerList == null || brokerList.isEmpty()) {
            throw new ConfigurationException("Broker List must be specified");
        }
        String acks = properties.getProperty(KafkaSenderConfiguration.REQUIRED_ACKS, "1");
        kafkaConf.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        kafkaConf.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaConf.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        kafkaConf.put(ProducerConfig.ACKS_CONFIG, acks);
        String metadataTimeoutMs = properties.getProperty(KafkaSenderConfiguration.METADATA_FETCH_TIMEOUT_MS
                , KafkaSenderConfiguration.DEFAULT_METADATA_FETCH_TIMEOUT_MS);
        String requestTimeoutMs = properties.getProperty(KafkaSenderConfiguration.REQUEST_TIMEOUT_MS
                , KafkaSenderConfiguration.DEFAULT_REQUEST_TIMEOUT_MS);
        kafkaConf.put(ProducerConfig.METADATA_FETCH_TIMEOUT_CONFIG, Integer.valueOf(metadataTimeoutMs));
        kafkaConf.put(ProducerConfig.TIMEOUT_CONFIG, Integer.valueOf(requestTimeoutMs));
        LOGGER.info(kafkaConf.toString());
        String deliveryModeName = properties.getProperty(KafkaSenderConfiguration.DELIVERY_STRATEGY, DeliveryMode.SYNC.name());
        String deliveryClassName = DeliveryMode.valueOf(deliveryModeName).toClassName();
        try {
            deliveryStrategy = (DeliveryStrategy) Class.forName(deliveryClassName).newInstance();
            if (deliveryStrategy instanceof BlockingDeliveryStrategy) {
                String timeout = properties.getProperty(KafkaSenderConfiguration.DELIVERY_TIMEOUT, KafkaSenderConfiguration.DEFAULT_DELIVERY_TIMEOUT);
                ((BlockingDeliveryStrategy) deliveryStrategy).setTimeout(Integer.valueOf(timeout));
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new ConfigurationException("DeliveryStrategy Class error, class name: " + deliveryClassName, e);
        }
        if(counter == null) {
            counter = new Counter(Counter.Type.KAFKA);
        }
    }

    @Override
    public void send(Event event) {
        String topicName = topic.get();
        if(topicName == null || topicName.isEmpty()) {
            throw new IllegalStateException("Topic was not specified. please set topic");
        }
        doSend(topicName, event);
    }

    @Override
    public void send(String topic, Event event) {
        doSend(topic, event);
    }

    private void doSend(String topic, Event event) {
        if(debugMode.equals(DebugMode.ON)) {
            LOGGER.info("print event: {}", new String(event.getBody()));
            return;
        }
        long startTime = System.currentTimeMillis();
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, null, event.getBody());
        try {
            deliveryStrategy.send(producer, record, event, (obj, e) -> LOGGER.error("Sending events to Kafka failed. event: " + obj, e));
            counter.incrementSuccessCount();
        } catch (Exception e) {
            counter.incrementFailedCount();
            throw e;
        } finally {
            counter.addAndGetSendTime(System.currentTimeMillis() - startTime);
        }
    }
}
