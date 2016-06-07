package com.xsl.data.collect.core;

import com.xsl.data.collect.metrics.Counter;

import java.util.Properties;

/**
 * Created by howard on 16/4/13.
 */
public abstract class AbstractSender implements Sender, LifecycleAware, Configurable {

    private String name;
    private LifecycleState lifecycleState;
    protected Counter counter;

    public AbstractSender() {
        lifecycleState = LifecycleState.IDLE;
    }

    @Override
    public void configure(Properties properties) {
        //sub class
    }

    @Override
    public void start() {
        lifecycleState = LifecycleState.START;
        counter.start();
    }

    @Override
    public void stop() {
        lifecycleState = LifecycleState.STOP;
        counter.stop();
    }

    @Override
    public LifecycleState getLifecycleState() {
        return lifecycleState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AbstractSender{" +
                "name='" + name + '\'' +
                '}';
    }
}
