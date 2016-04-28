package com.xsl.data.collect.test;

import java.time.LocalDate;

/**
 * Created by howard on 16/4/28.
 */
public class LocalDateTest {

    public static void main(String[] args) {
        LocalDate curr = LocalDate.now();
        LocalDate lastDay = curr.plusDays(-1);
        LocalDate beforeDay = curr.plusDays(-2);

        System.out.println(lastDay.isAfter(beforeDay));
    }
}
