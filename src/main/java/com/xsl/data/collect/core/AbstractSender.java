package com.xsl.data.collect.core;

import java.util.Properties;

/**
 * Created by howard on 16/4/13.
 */
public abstract class AbstractSender implements Sender, LifecycleAware, Configurable {

    private String name;
    private LifecycleState lifecycleState;

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
    }

    @Override
    public void stop() {
        lifecycleState = LifecycleState.STOP;
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
