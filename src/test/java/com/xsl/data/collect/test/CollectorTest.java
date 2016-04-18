package com.xsl.data.collect.test;

import com.xsl.data.collect.Collector;
import org.junit.Test;

/**
 * Created by howard on 16/4/14.
 */
public class CollectorTest {

    @Test
    public void testCreate() {
        Collector.Builder builder = new Collector.Builder();
        Collector collector = builder.build();
    }
}
