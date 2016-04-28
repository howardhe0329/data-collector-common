package com.xsl.data.collect.test;

import java.io.File;

/**
 * Created by howard on 16/4/28.
 */
public class FileTest1 {

    public static void main(String[] args) {
        String directy = "/Users/howard/log/test";

        System.out.println(directy.indexOf('\u0000'));
        File directyFile = new File(directy);
        if(!directyFile.exists()) {
            System.out.println(directyFile.mkdirs());
        }

    }
}
