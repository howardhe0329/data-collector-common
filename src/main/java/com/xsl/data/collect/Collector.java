package com.xsl.data.collect;

import com.xsl.data.collect.common.CommonConfiguration;
import com.xsl.data.collect.common.DebugMode;
import com.xsl.data.collect.common.SendMode;
import com.xsl.data.collect.core.Sender;
import com.xsl.data.collect.event.Event;
import com.xsl.data.collect.kafka.KafkaSender;
import com.xsl.data.collect.local.LocalSender;
import com.xsl.data.collect.util.ConfigurationProvider;

import java.util.Properties;

/**
 * Created by howard on 16/4/14.
 */
public class Collector {

    private Sender sender;

    public Collector(Sender sender) {
        sender.start();
        this.sender = sender;
    }

    public void stop() {
        sender.stop();
    }

    public void collect(Event event) {
        sender.send(event);
    }

    public void collect(String name, Event event) {
        sender.send(name, event);
    }

    public static class Builder {

        private Boolean debug;
        private SendMode sendMode;
        private Sender sender;

        public Builder isDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder sendMode(SendMode sendMode) {
            this.sendMode = sendMode;
            return this;
        }

        public Collector build() {
            if (sendMode == null) {
                sendMode = SendMode.KAFKA;
            }
            if (sendMode.equals(SendMode.KAFKA)) {
                sender = new KafkaSender();
            } else if (sendMode.equals(SendMode.LOCAL)) {
                sender = new LocalSender();
            }
            ConfigurationProvider provider = new ConfigurationProvider();
            Properties properties = provider.loads();
            if (debug != null) {
                properties.setProperty(CommonConfiguration.DEBUG_MODE, debug ? DebugMode.ON.name() : DebugMode.OFF.name());
            }
            sender.configure(properties);
            return new Collector(sender);
        }

    }
}
