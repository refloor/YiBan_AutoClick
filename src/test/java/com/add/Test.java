package com.add;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;


import java.io.UnsupportedEncodingException;
import java.util.Date;


public class Test {

    @org.junit.Test
    public void test1() throws UnsupportedEncodingException {
        String s = RandomUtil.randomUUID();
        String t = "68c8a272b458dd09b19972559c5bc13b";
        System.out.println(t);
        s = s.replace("-", "");
        System.out.println(s);

    }
}
