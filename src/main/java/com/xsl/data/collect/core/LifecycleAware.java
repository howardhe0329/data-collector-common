package com.xsl.data.collect.core;

/**
 * 生命周期
 * Created by howard on 16/4/13.
 */
public interface LifecycleAware {

    /**
     * start
     */
    void start();

    /**
     * stop
     */
    void stop();

    /**
     * state
     * @return
     */
    LifecycleState getLifecycleState();
}
