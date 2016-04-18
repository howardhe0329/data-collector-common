package com.xsl.data.collect.kafka;

import com.xsl.data.collect.event.Event;
import org.apache.kafka.clients.producer.BufferExhaustedException;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.*;

/**
 * Created by howard on 16/4/13.
 */
public class BlockingDeliveryStrategy implements DeliveryStrategy {

    private long timeout = 0L;

    @Override
    public <K, V, E> boolean send(Producer<K, V> producer, ProducerRecord<K, V> record, Event event, FailedDeliveryCallback<Event> callback) {
        try {
            Future<RecordMetadata> future = producer.send(record);
            if (timeout > 0L)
                future.get(timeout, TimeUnit.MILLISECONDS);
            else if (timeout == 0L) {
                future.get();
            }
            return true;
        } catch (InterruptedException e) {
            return false;
        } catch (TimeoutException | BufferExhaustedException | ExecutionException | CancellationException e) {
            callback.onFailedDelivery(event, e);
            return false;
        }
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
