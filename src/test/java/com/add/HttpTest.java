package com.add;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import org.junit.Test;

public class HttpTest {

    @Test
    public void test1() {
        String url = "https://www.baidu.com";
        HttpResponse resp = HttpRequest.post(url).execute();
        String cookie = resp.getCookieStr().split(";")[0].split("=")[1];
        System.out.println(cookie);
    }
}
