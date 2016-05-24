package com.xsl.data.collect.local;

import com.xsl.data.collect.Collector;
import com.xsl.data.collect.common.CommonConfiguration;
import com.xsl.data.collect.common.DebugMode;
import com.xsl.data.collect.core.Sender;
import com.xsl.data.collect.event.Event;
import com.xsl.data.collect.util.ConfigurationProvider;

import java.util.Properties;

/**
 * Created by howard on 16/4/28.
 */
public class LocalCollector implements Collector {

    private Sender sender;

    private LocalCollector(Sender sender) {
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
        private LocalConf localConf;

        public Builder isDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder conf(LocalConf localConf) {
            this.localConf = localConf;
            return this;
        }

        public LocalCollector build() {
            sender = new LocalSender();
            ConfigurationProvider provider = new ConfigurationProvider();
            Properties properties = provider.loads();
            if (debug != null) {
                properties.setProperty(CommonConfiguration.DEBUG_MODE, debug ? DebugMode.ON.name() : DebugMode.OFF.name());
            }
            if(localConf != null) {
                localConf.toPreperties(properties);
            }
            sender.configure(properties);
            sender.start();
            return new LocalCollector(sender);
        }
    }
}
