package com.add;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.add.domain.Clock;
import com.add.domain.MorningClock;
import com.add.domain.NoonClock;
import com.add.domain.User;
import com.add.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

public class Main1 {
    private static final Logger logger = LoggerFactory.getLogger(Main1.class);
    private static final File userFile;
    private static final String userFileStr;
    public static List<User> users;

    static {
        //初始化配置文件
        ClassPathResource resource = new ClassPathResource("data.json");
        userFile = new File(resource.getAbsolutePath());
        userFileStr = resource.readUtf8Str();
        //解析所有用户
        loadUsers();
    }


    //加载所有打卡用户
    private static void loadUsers() {
        try {
            //加载配置文件
            String usersStr = JSONUtil.parseObj(userFileStr).getStr("users");
            JSONArray objects = JSONUtil.parseArray(usersStr);
            users = objects.toList(User.class);
        } catch (Exception e) {
            String msg = String.format("fail to load or parse, file is {%s}", userFile.getName());
            logger.error(msg, e);
            throw new BaseException(1, msg, e);
        }

    }


    public static void main(String[] args) throws UnsupportedEncodingException {

        Clock morningClock = new MorningClock();
        Clock noonClock = new NoonClock();
        //当前时间
        Date now = DateUtil.date();
        System.out.println(now);
        for (User user : users) {
            if (user.getMorningClick() && morningClock.validTime(now)) {
                morningClock.clock(user);
            }
            if (user.getNoonClick() && noonClock.validTime(now)) {
                noonClock.clock(user);
            }
        }

    }
}
