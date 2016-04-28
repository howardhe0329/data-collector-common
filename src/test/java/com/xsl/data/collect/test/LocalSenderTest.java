package com.xsl.data.collect.test;

import com.xsl.data.collect.core.Sender;
import com.xsl.data.collect.local.LocalSender;
import com.xsl.data.collect.util.ConfigurationProvider;
import com.xsl.data.collect.util.EventBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Random;

/**
 * Created by howard on 16/4/28.
 */
public class LocalSenderTest {

    private Sender sender;

    @Before
    public void setUp() {
        sender = new LocalSender();
        ConfigurationProvider provider = new ConfigurationProvider();
        sender.configure(provider.loads());
        sender.start();
    }

    @After
    public void setDown() {
        sender.stop();
    }

    @Test
    public void testSend() {
        sender.send(EventBuilder.withBody("test...", Charset.defaultCharset()));
        sender.send(EventBuilder.withBody("杏树林, " + System.currentTimeMillis(), Charset.defaultCharset()));
    }

    @Test
    public void testMutliThreadSend() {
        Random random = new Random();
        Thread[] threads = new Thread[5];
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    sender.send(EventBuilder.withBody(Thread.currentThread().getName() + ": " + j, Charset.defaultCharset()));
//                    try {
//                        Thread.sleep(random.nextInt(100));
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            });
            threads[i] = thread;
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long elaseTime = System.currentTimeMillis() - startTime;
        System.out.println(elaseTime);
    }

    @Test
    public void testTimeSend() {
        Random random = new Random();
        Thread[] threads = new Thread[5];
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    sender.send(EventBuilder.withBody(Thread.currentThread().getName() + ": " + j, Charset.defaultCharset()));
                    try {
                        Thread.sleep(random.nextInt(3000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[i] = thread;
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long elaseTime = System.currentTimeMillis() - startTime;
        System.out.println(elaseTime);
    }


}
