package com.add;

import cn.hutool.core.date.DateUtil;


import java.io.UnsupportedEncodingException;
import java.util.Date;


public class Test {

    @org.junit.Test
    public void test1() throws UnsupportedEncodingException {
        Date now = DateUtil.date();
        String dateStr = "2021-05-01 22:33:23";
        Date date = DateUtil.parse(dateStr);
        System.out.println(now);
        System.out.println(date);
        long day = DateUtil.betweenDay(date, now, true);
        System.out.println(day);

    }
}
