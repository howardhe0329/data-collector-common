package com.xsl.data.collect.metrics;

import com.xsl.data.collect.core.LifecycleAware;
import com.xsl.data.collect.core.LifecycleState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by howard on 16/6/7.
 */
public class Counter implements CounterMBean, LifecycleAware {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private LifecycleState lifecycleState;
    private AtomicLong successCount = new AtomicLong(0L);
    private AtomicLong failedCount = new AtomicLong(0L);
    private AtomicLong sendTime = new AtomicLong(0L);
    private Type type;

    public Counter(Type type) {
        lifecycleState = LifecycleState.IDLE;
        this.type = type;
    }

    public long incrementSuccessCount() {
        return successCount.getAndIncrement();
    }

    public long incrementFailedCount() {
        return  failedCount.getAndIncrement();
    }

    public long addAndGetSendTime(long delta) {
        return  sendTime.addAndGet(delta);
    }

    @Override
    public long getSuccessCount() {
        return successCount.get();
    }

    @Override
    public long getFailedCount() {
        return failedCount.get();
    }

    @Override
    public long getSendTimer() {
        return sendTime.get();
    }

    @Override
    public void start() {
        register();
        lifecycleState = LifecycleState.START;
    }

    @Override
    public void stop() {
        LOGGER.info("Shutdown Metric for type: " + type.name());
        lifecycleState = LifecycleState.STOP;
    }

    @Override
    public LifecycleState getLifecycleState() {
        return lifecycleState;
    }

    @Override
    public String getType() {
        return type.name();
    }

    private void register() {
        String className = this.getClass().getName();
        String mBeanName = className + ":type=" + type.name();
        try {
            ObjectName objectName = new ObjectName(mBeanName);
            if(ManagementFactory.getPlatformMBeanServer().isRegistered(objectName)) {
                ManagementFactory.getPlatformMBeanServer().unregisterMBean(objectName);
            }
            ManagementFactory.getPlatformMBeanServer().registerMBean(this, objectName);
        } catch (Exception e) {
            LOGGER.error("Failed to register monitored counter for type: " + type.name());
        }
    }

    public enum Type {
        KAFKA, LOCAL;
    }
}
