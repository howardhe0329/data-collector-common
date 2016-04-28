package com.xsl.data.collect.test;

import com.xsl.data.collect.Collector;
import com.xsl.data.collect.local.LocalCollector;
import com.xsl.data.collect.local.LocalConf;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by howard on 16/4/28.
 */
public class LocalCollectorTest {

    private Collector collector;

    @Before
    public void setUp() {
        collector = new LocalCollector.Builder().conf(new LocalConf().directory("log/collect").fileName("test.log")).build();
    }

    @After
    public void setDown() {
        collector.shutdown();
    }

    @Test
    public void testCollect() {
        collector.collect("test");
    }
}
