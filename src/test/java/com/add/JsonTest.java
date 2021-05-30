package com.add;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.add.domain.User;
import org.junit.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JsonTest {
    public final String json = "{\n" +
            "    \"last_update_day\": 28,\n" +
            "    \"data\": [\n" +
            "        {\n" +
            "            \"name\": \"Nick\",\n" +
            "            \"id\": \"1421191634\",\n" +
            "            \"url\": \"http://yiban.sust.edu.cn/v4/public/index.php?key=m53QXuGNaHDHsrrf//utmo27Xm_EnsQS7aVIbWNr0xKAvwYeYk54AizzNJDKtjYFgIx6ZoAvTsuROwIYdKCDDayyIo3YuslFei4FAnvvIVIOBI9kGY/UmFPwntPSFinU5SANbOQnV8JVJJryH7HbMQaraKCy4ivF4inCYoFeaM0=\",\n" +
            "            \"location\": \"陕西省西安市 未央区 111县道 111县 靠近北城驾校 + &\",\n" +
            "            \"morning_check\": true,\n" +
            "            \"noon_check\": true\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    @Test
    public void Test1(){
        File file = new File("data.json");
        JSON jsonStr = JSONUtil.readJSON(file, Charset.forName("utf-8"));
        String usersStr = JSONUtil.parseObj(jsonStr).getStr("users");
        JSONArray objects = JSONUtil.parseArray(usersStr);

        List<User> users = objects.toList(User.class);

        for (User user : users) {
            System.out.println(user);
        }
    }


    @Test
    public void Test2(){
        File file = new File("data.json");
        JSON jsonStr = JSONUtil.readJSON(file, Charset.forName("utf-8"));
        String usersStr = JSONUtil.parseObj(jsonStr).getStr("users");
        JSONArray objects = JSONUtil.parseArray(usersStr);

        List<User> users = objects.toList(User.class);
        User user = users.get(0);
        JSONObject resJsonObject = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", user);
        map.put("start_time", DateUtil.now());
        map.put("finish_time", DateUtil.now());
        map.put("status_code", 1);
        map.put("status_msg", "error");

        resJsonObject.putAll(map);
        String json = resJsonObject.toString();
        System.out.println(json);
    }

    @Test
    public void Test3() throws UnsupportedEncodingException {
        File file = new File("data.json");
        JSON jsonStr = JSONUtil.readJSON(file, Charset.forName("utf-8"));
        String usersStr = JSONUtil.parseObj(jsonStr).getStr("users");
        JSONArray objects = JSONUtil.parseArray(usersStr);

        List<User> users = objects.toList(User.class);
        User user = users.get(0);

        Map<String, Object> requestBody = Main.getClickRequestBody(user, 24);
        System.out.println(json);
    }


}
