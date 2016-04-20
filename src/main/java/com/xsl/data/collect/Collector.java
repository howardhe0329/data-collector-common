package com.xsl.data.collect;

import com.xsl.data.collect.event.Event;

/**
 * Created by howard on 16/4/18.
 */
public interface Collector {

    /**
     * 数据收集
     * @param event 数据
     */
    void collect(Event event);

    /**
     * 数据收集
     * @param name topic
     * @param event 数据
     */
    void collect(String name, Event event);

    /**
     * 关闭
     */
    void shutdown();

}
