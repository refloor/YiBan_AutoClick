package com.add.domain;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.add.util.EmailUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@ToString
@Getter
@Setter
public abstract class Clock {
    static String DEFAULT_LOCATION = "陕西省西安市 未央区 111县道 111县 靠近北城驾校";

    String name;
    String startTime;
    String endTime;
    int code;
    String reqUrl;

    abstract  Map<String, Object> getClockRequestBody(User user) throws UnsupportedEncodingException;
    abstract String getClockRequestUrl();

    //获取session
    String getSession(User user) {
        try {
            String url = user.getUrl();
            HttpResponse resp = HttpRequest
                    .post(url)
                    .disableCookie()
                    .execute();
            String cookie = resp.getCookieStr().split(";")[0].split("=")[1];
            return cookie;
        } catch (Exception e) {
            return null;
        }
    }



    public void clock(User user) throws UnsupportedEncodingException {
        //获取session
        String session = null;
        for (int i = 0 ; i < 3 ; i ++) {
            session = getSession(user);
            if (session != null)
                break;
        }
        //获取打卡链接
        String url = getClockRequestUrl();
        //获取打卡请求体
        Map<String, Object> requestBodyMap = getClockRequestBody(user);
        //构建打卡请求
        HttpResponse response = null;
        HttpRequest request = HttpRequest.post(url)
                .contentType("application/x-www-form-urlencoded")
                .charset("utf-8")
                .header(Header.CONNECTION, "keep-alive", true)
                .header("X-Requested-With", "XMLHttpRequest", true)
                .form(requestBodyMap)
                .header(Header.COOKIE, "PHPSESSID" + "=" + session + ";" + "client=android")
                .timeout(2000);

        //doClock
        boolean res = doClock(user, request);

        if (res == true && user.getSendEmail() == true) {
            sendSuccessEmail(user);
        } else if (res == false && user.getSendEmail() == true) {
            sendFailureEmail(user);
        }

    }


    public boolean doClock(User user, HttpRequest request) {
        HttpResponse response;
        for (int i = 0 ; i < 3 ; i ++) {
            response = request.execute();
            if (response != null) {
                Integer resCode = (Integer) JSONUtil.parseObj(response.body()).get("code");
                if (request == null || resCode != 1) {
                    String msg = String.format("{%s} fail, time is {%s}, id is {%s}, name is {%s}, code is {%s}, response is {%s}", getName(), DateUtil.now(), user.getId(), user.getName(), getCode(), response);
                    continue;
                }
                String msg = String.format("{%s} success, time is {%s}, id is {%s}, name is {%s}, code is {%s}, response is {%s}", getName(), DateUtil.now(), user.getId(), user.getName(), getCode(), response);
                System.out.println(msg);
                return true;
            }
        }
        return false;
    }

    //判断时间是否在有效时间内
    public boolean validTime(Date date) {
        String now = DateUtil.formatTime(date);
        if (getStartTime().compareTo(now) <= 0 && getEndTime().compareTo(now) >= 0)
            return true;
        return false;
    }

    //发送成功打卡邮件
    public void sendSuccessEmail(User user) {
        String email = user.getEmail();
        String title = getName() + "成功!";
        String content = user.getName() + "," + getName() +"打卡成功。\n\r";
        Date now = DateUtil.date();
        content += now;
        if (email != null) {
            EmailUtil.send(email, title, content);
        }
    }

    public void sendFailureEmail(User user) {
        String email = user.getEmail();
        String title = getName() + "失败!";
        String content = user.getName() + "," + getName() +"打卡失败。\n\r";
        Date now = DateUtil.date();
        content += now;
        if (email != null) {
            EmailUtil.send(email, title, content);
        }
    }




}
