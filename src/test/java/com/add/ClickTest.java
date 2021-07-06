package com.add;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.add.domain.User;
import org.junit.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClickTest {

    @Test
    public void test1() throws UnsupportedEncodingException {
//        //获取user
//        File file = new File("data.json");
//        JSON jsonStr = JSONUtil.readJSON(file, Charset.forName("utf-8"));
//        String usersStr = JSONUtil.parseObj(jsonStr).getStr("users");
//        JSONArray objects = JSONUtil.parseArray(usersStr);
//        List<User> users = objects.toList(User.class);
//        User user = users.get(0);
//        System.out.println(user);
//
//        //获取Session
//        String cookie = Main.getCookie(user);
//        System.out.println("cookie:" + cookie);
//        //请求
//        String url = "http://yiban.sust.edu.cn/v4/public/index.php/Index/formflow/add.html?desgin_id=24&list_id=12";
//        Map<String, Object> requestBodyMap = new HashMap<String, Object>();
//        requestBodyMap.put(encode("24[0][0][name]"), encode("form[24][field_1588749561_2922][]"));requestBodyMap.put(encode("24[0][0][value]"), encode("36.3"));
//        requestBodyMap.put(encode("24[0][1][name]"), encode("form[24][field_1588749738_1026][]"));requestBodyMap.put(encode("24[0][1][value]"), encode("陕西省西安市 未央区 111县道 111县 靠近北城驾校"));
//        requestBodyMap.put(encode("24[0][2][name]"), encode("form[24][field_1588749759_6865][]"));requestBodyMap.put(encode("24[0][2][value]"), encode("是"));
//        requestBodyMap.put(encode("24[0][3][name]"), encode("form[24][field_1588749842_2715][]"));requestBodyMap.put(encode("24[0][3][value]"), encode("否"));
//        requestBodyMap.put(encode("24[0][4][name]"), encode("form[24][field_1588749886_2103][]"));requestBodyMap.put(encode("24[0][4][value]"), encode("1"));
//
//        System.out.println(requestBodyMap);
//        //转码
//        Map<String, Object> encodeRequestBodyMap = new HashMap<String, Object>();
//        for (String key : requestBodyMap.keySet()) {
//            String v = (String) requestBodyMap.get(key);
//            encodeRequestBodyMap.put(new String(key.getBytes(), "UTF-8"), new String(v.getBytes(), "UTF-8"));
//
//        }
//
//        HttpResponse response = null;
//        HttpRequest request = HttpRequest.post(url)
//                .contentType("application/x-www-form-urlencoded")
//                .charset("utf-8")
//                .header(Header.CONNECTION, "keep-alive", true)
//                .header("X-Requested-With", "XMLHttpRequest", true)
//                .form(requestBodyMap)
//                .header(Header.COOKIE, "PHPSESSID" + "=" + cookie + ";" + "client=android")
//                .timeout(2000);
//
//        response = request.execute();
//        System.out.println(response);
    }

    private String encode(String s) throws UnsupportedEncodingException {
        return new String(s.getBytes(), "UTF-8");
    }
}
