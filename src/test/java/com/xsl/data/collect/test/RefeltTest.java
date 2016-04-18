package com.xsl.data.collect.test;

import com.xsl.data.collect.kafka.BlockingDeliveryStrategy;
import com.xsl.data.collect.kafka.DeliveryStrategy;

/**
 * Created by howard on 16/4/14.
 */
public class RefeltTest {

    public static void main(String[] args) {
        try {
            DeliveryStrategy deliveryStrategy = (DeliveryStrategy) Class.forName("com.xsl.data.collect.kafka.BlockingDeliveryStrategy").newInstance();
            System.out.println(deliveryStrategy.getClass().getName());
            if (deliveryStrategy instanceof BlockingDeliveryStrategy) {
                ((BlockingDeliveryStrategy) deliveryStrategy).setTimeout(100);

                System.out.println(((BlockingDeliveryStrategy) deliveryStrategy).getTimeout());
            }
            System.out.println(BlockingDeliveryStrategy.class.getName());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
