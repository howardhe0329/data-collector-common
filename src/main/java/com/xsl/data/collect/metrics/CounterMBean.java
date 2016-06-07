package com.xsl.data.collect.metrics;

/**
 *
 * Created by howard on 16/6/6.
 */
public interface CounterMBean {

    /**
     * 发送成功的数量
     * @return
     */
    long getSuccessCount();

    /**
     * 发送失败的数量
     * @return
     */
    long getFailedCount();

    /**
     * 发送总耗时(单位是秒)
     * @return
     */
    long getSendTimer();

    /**
     *
     * @return
     */
    String getType();

}
