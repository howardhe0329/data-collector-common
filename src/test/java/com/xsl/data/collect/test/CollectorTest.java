package com.xsl.data.collect.test;

import com.xsl.data.collect.kafka.KafkaCollector;
import org.junit.Test;

/**
 * Created by howard on 16/4/14.
 */
public class CollectorTest {

    @Test
    public void testCreate() {
        KafkaCollector.Builder builder = new KafkaCollector.Builder();
        KafkaCollector collector = builder.build();
    }
}
