package com.xsl.data.collect.test;

/**
 * Created by howard on 16/6/7.
 */
public class ClassTest {

    public static void main(String[] args) {
        System.out.println(ClassTest.class.getName());
        System.out.println(ClassTest.class.getCanonicalName());
        System.out.println(ClassTest.class.getSimpleName());
        System.out.println(ClassTest.class.getTypeName());
        System.out.println(ClassTest.class.getPackage().getName());
    }
}
