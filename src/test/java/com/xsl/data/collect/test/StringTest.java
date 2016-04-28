package com.xsl.data.collect.test;

/**
 * Created by howard on 16/4/28.
 */
public class StringTest {

    public static void main(String[] args) {
        String fileName = "test.2016-04-28.log";
        int pos = fileName.lastIndexOf(".");
        System.out.println(fileName.substring(0, pos + 1));
        System.out.println(fileName.substring(pos + 1));
    }
}
