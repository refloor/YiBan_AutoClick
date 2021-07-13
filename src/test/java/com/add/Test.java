package com.add;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.add.domain.Clock;
import com.add.domain.MorningClock;
import com.add.domain.NoonClock;
import com.add.domain.User;


import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;


public class Test {

    @org.junit.Test
    public void test1() throws UnsupportedEncodingException {
        Clock morningClock = new MorningClock();
        Clock noonClock = new NoonClock();
        Date now = DateUtil.date();
        now.setHours(6);now.setMinutes(0);now.setSeconds(0);
        System.out.println(now);
        System.out.println(morningClock.validTime(now));
        System.out.println(noonClock.validTime(now));
        System.out.println("---------------");
        now.setHours(8);now.setMinutes(59);now.setSeconds(0);
        System.out.println(now);
        System.out.println(morningClock.validTime(now));
        System.out.println(noonClock.validTime(now));
        System.out.println("---------------");
        now.setHours(9);now.setMinutes(0);now.setSeconds(1);
        System.out.println(now);
        System.out.println(morningClock.validTime(now));
        System.out.println(noonClock.validTime(now));
        System.out.println("---------------");
        now.setHours(12);now.setMinutes(0);now.setSeconds(0);
        System.out.println(now);
        System.out.println(morningClock.validTime(now));
        System.out.println(noonClock.validTime(now));
        System.out.println("---------------");
        now.setHours(12);now.setMinutes(0);now.setSeconds(05);
        System.out.println(now);
        System.out.println(morningClock.validTime(now));
        System.out.println(noonClock.validTime(now));
        System.out.println("---------------");
        now.setHours(14);now.setMinutes(50);now.setSeconds(0);
        System.out.println(now);
        System.out.println(morningClock.validTime(now));
        System.out.println(noonClock.validTime(now));
        System.out.println("---------------");
        now.setHours(15);now.setMinutes(1);now.setSeconds(1);
        System.out.println(now);
        System.out.println(morningClock.validTime(now));
        System.out.println(noonClock.validTime(now));
        System.out.println("---------------");
    }

    @org.junit.Test
    public void test2() {
        List<User> users = Main1.users;
        Clock morningClock = new MorningClock();
        Clock noonClock = new NoonClock();
        morningClock.sendFailureEmail(users.get(0));
    }
}
