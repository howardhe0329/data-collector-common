package com.xsl.data.collect.kafka;

import com.xsl.data.collect.event.Event;
import org.apache.kafka.clients.producer.BufferExhaustedException;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Created by howard on 16/4/13.
 */
public class AsynchronousDeliveryStrategy implements DeliveryStrategy {


    @Override
    public <K, V, E> boolean send(Producer<K, V> producer, ProducerRecord<K, V> record, Event event, FailedDeliveryCallback<Event> callback) {
        try {
            producer.send(record, ((metadata, exception) -> {
                if (exception != null) {
                    callback.onFailedDelivery(event, exception);
                }
            }));
            return true;
        } catch (BufferExhaustedException e) {
            callback.onFailedDelivery(event, e);
            return false;
        }
    }
}
