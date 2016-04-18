package com.xsl.data.collect.kafka;

import com.xsl.data.collect.event.Event;

/**
 * 失败处理接口
 * Created by howard on 16/4/13.
 */
public interface FailedDeliveryCallback<E extends Event> {

    /**
     * 发送消息失败处理
     * @param event 事件
     * @param e
     */
    void onFailedDelivery(E event, Throwable e);
}
