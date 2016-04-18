package com.xsl.data.collect.local;

import com.xsl.data.collect.common.SendMode;
import com.xsl.data.collect.core.AbstractSender;
import com.xsl.data.collect.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by howard on 16/4/14.
 */
public class LocalSender extends AbstractSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalSender.class);

    public LocalSender() {
        setName(SendMode.LOCAL.name());
    }

    @Override
    public void start() {
        LOGGER.info("Start Local Sender: "+ getName());

        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void configure(Properties properties) {
        //TODO
    }

    @Override
    public void send(Event event) {

    }

    @Override
    public void send(String topic, Event event) {

    }
}
