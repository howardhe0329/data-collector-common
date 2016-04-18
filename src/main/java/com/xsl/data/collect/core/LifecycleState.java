package com.xsl.data.collect.core;

/**
 * Created by howard on 16/4/13.
 */
public enum LifecycleState {

    IDLE, START, STOP, ERROR;

    public static final LifecycleState[] START_OR_ERROR = new LifecycleState[] {
            START, ERROR };
    public static final LifecycleState[] STOP_OR_ERROR = new LifecycleState[] {
            STOP, ERROR };
}
