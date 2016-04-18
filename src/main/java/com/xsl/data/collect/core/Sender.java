package com.xsl.data.collect.core;

import com.xsl.data.collect.event.Event;

/**
 * 发送器
 * Created by howard on 16/4/13.
 */
public interface Sender extends LifecycleAware, Configurable {

    /**
     * 发送消息
     * @param event
     */
    void send(Event event);

    /**
     * 发送消息
     * @param topic
     * @param event
     */
    void send(String topic, Event event);
}
