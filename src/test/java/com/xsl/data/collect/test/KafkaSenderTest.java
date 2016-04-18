package com.xsl.data.collect.test;

import com.xsl.data.collect.util.ConfigurationProvider;
import com.xsl.data.collect.kafka.KafkaSender;
import com.xsl.data.collect.util.EventBuilder;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * Created by howard on 16/4/14.
 */
public class KafkaSenderTest {

    @Test
    public void testSend() {
        KafkaSender kafkaSender = new KafkaSender();
        ConfigurationProvider provider = new ConfigurationProvider();
        kafkaSender.configure(provider.loads());
        System.out.println(kafkaSender.getLifecycleState());
        kafkaSender.start();
        System.out.println(kafkaSender.getLifecycleState());
        String message = "杏树林";
        kafkaSender.send(EventBuilder.withBody(message, Charset.defaultCharset()));
        kafkaSender.stop();
        System.out.println(kafkaSender.getLifecycleState());
    }
}
