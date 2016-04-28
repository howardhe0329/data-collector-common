package com.xsl.data.collect;

import com.xsl.data.collect.event.Event;
import com.xsl.data.collect.util.EventBuilder;

import java.nio.charset.Charset;

/**
 * Created by howard on 16/4/18.
 */
public interface Collector {

    /**
     * 数据收集
     *
     * @param event 数据
     */
    void collect(Event event);

    /**
     * 数据收集
     *
     * @param name  topic
     * @param event 数据
     */
    void collect(String name, Event event);

    /**
     * 数据收集
     *
     * @param data
     */
    default void collect(String data) {
        collect(EventBuilder.withBody(data, Charset.defaultCharset()));
    }

    /**
     * 数据收集
     *
     * @param name
     * @param data
     */
    default void collect(String name, String data) {
        collect(name, EventBuilder.withBody(data, Charset.defaultCharset()));
    }

    /**
     * 关闭
     */
    void shutdown();

}
