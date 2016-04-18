package com.xsl.data.collect.kafka;

import com.xsl.data.collect.event.Event;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * 发送策略接口
 * Created by howard on 16/4/13.
 */
public interface DeliveryStrategy {

    /**
     * 向kafka发送消息
     * @param producer
     * @param record
     * @param event
     * @param callback
     * @param <K>
     * @param <V>
     * @param <E>
     * @return
     */
    <K, V, E> boolean send(Producer<K, V> producer, ProducerRecord<K, V> record, Event event, FailedDeliveryCallback<Event> callback);
}
