package com.add;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.add.domain.Email;
import com.add.domain.ExceptionStatus;
import com.add.domain.User;
import com.add.exception.BaseException;
import com.add.util.EmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final File userFile;
    private static final String userFileStr;
    private static final int MORNING_CLICK_CODE = 24;
    private static final int NOON_CLICK_CODE = 25;
    private static final String DEFAULT_LOCATION = "陕西省西安市 未央区 111县道 111县 靠近北城驾校";
    private static final long DEFAULT_URL_WARN_DAY = 15;

    private static List<User> users;

    static {
        //初始化配置文件
        ClassPathResource resource = new ClassPathResource("data.json");
        userFile = new File(resource.getAbsolutePath());
        userFileStr = resource.readUtf8Str();
    }

    //初始化
    private static void init() {
        //加载所有的用户
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



    //给指定用户打卡
    private static JSONObject click(User user) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", user);
        jsonObject.put("has_morning_click", false);
        jsonObject.put("has_noon_click", false);
        int sumClickCount = user.getClickSumCount();
        int clickCount = 0;
        boolean hasMorningClick = false;
        boolean hasNoonClick = false;

        try {
            //特判配置用户无需打卡
            if (sumClickCount == 0) {
                String msg = String.format("need not click, all click choices is false, time is {%s}, id is {%s}, name is {%s}", DateUtil.now(), user.getId(), user.getName());
                throw new BaseException(ExceptionStatus.NOT_CONSUME, msg);
            }

            //尝试获取cookie
            String cookie = null;
            for (int i = 0 ; i < 3 ; i ++) {
                cookie = getCookie(user);
                if (cookie != null)
                    break;
            }
            if (cookie == null) {
                String msg = String.format("fail to get cookie, time is {%s}, id is {%s}, name is {%s}, url is {%s}", DateUtil.now(), user.getId(), user.getUrl());
                throw new BaseException(ExceptionStatus.CONNECT_FAIL, msg);
            }

            //早打卡
            if (user.getMorningClick() == true) {
                hasMorningClick = doClick(user, cookie, MORNING_CLICK_CODE);
                if (hasMorningClick == true) clickCount ++;
            }
            //午打卡
            if (user.getNoonClick() == true) {
                hasNoonClick = doClick(user, cookie, NOON_CLICK_CODE);
                if (hasNoonClick == true) clickCount ++;
            }

            //全部打卡失败
            if (clickCount == 0) {
                String msg = String.format("all clicks fail, time is {%s}, id is {%s}, name is {%s}, url is {%s}", DateUtil.now(), user.getId(), user.getName(), user.getUrl());
                throw new BaseException(ExceptionStatus.CLICK_ALL_FAIL, msg);
            }

            //部分打卡失败
            if (clickCount < sumClickCount) {
                String msg = String.format("part clicks fail, time is {%s}, id is {%s}, name is {%s}, url is {%s}", DateUtil.now(), user.getId(), user.getName(), user.getUrl());
                if (user.getMorningClick() == true && hasMorningClick == false)
                    msg += "morning click fail.";
                if (user.getNoonClick() == true && hasNoonClick == false)
                    msg += "noon click fail.";
                throw new BaseException(ExceptionStatus.CLICK_PART_FAIL, msg);
            }
            jsonObject.put("status_code", null);
            jsonObject.put("status_msg", null);
        } catch (BaseException e) {
            jsonObject.put("status_code", e.getCode());
            jsonObject.put("status_msg", e.getMessage());
        } catch (Exception e) {
            jsonObject.put("status_code", ExceptionStatus.UNKNOWN_ERROR);
            jsonObject.put("status_msg", String.format("time is {%s}, ", DateUtil.now()) + e.getCause());
        } finally {
            return jsonObject;
        }


    }

    public static boolean doClick(User user, String cookie, int code) throws UnsupportedEncodingException {
        //获取打卡请求http链接
        String url = getClickRequestUrl(code);
        Map<String, Object> requestBodyMap = getClickRequestBody(user, code);
        //构建请求并请求
        try {

            HttpResponse response = null;
            HttpRequest request = HttpRequest.post(url)
                    .contentType("application/x-www-form-urlencoded")
                    .charset("utf-8")
                    .header(Header.CONNECTION, "keep-alive", true)
                    .header("X-Requested-With", "XMLHttpRequest", true)
                    .form(requestBodyMap)
                    //.header(Header.COOKIE,"client=android")
                    .header(Header.COOKIE, "PHPSESSID" + "=" + cookie + ";" + "client=android")
                    .timeout(2000);

            for (int i = 0 ; i < 3 ; i ++) {
                response = request.execute();
                if (response != null) {
                    String clickName = "click";
                    if (code == MORNING_CLICK_CODE) clickName = "morning click";
                    else if (code == NOON_CLICK_CODE) clickName = "noon click";
                    Integer resCode = (Integer) JSONUtil.parseObj(response.body()).get("code");
                    if (request == null || resCode != 1) {
                        String msg = String.format("{%s} fail, time is {%s}, id is {%s}, name is {%s}, code is {%s}, response is {%s}", clickName, DateUtil.now(), user.getId(), user.getName(), code, response);
                        logger.info(msg);
                        continue;
                    }
                    String msg = String.format("{%s} success, time is {%s}, id is {%s}, name is {%s}, code is {%s}, response is {%s}", clickName, DateUtil.now(), user.getId(), user.getName(), code, response);
                    System.out.println(msg);
                    logger.info(msg);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }

    }

    public static Map<String, Object> getClickRequestBody(User user, int code) throws UnsupportedEncodingException {
        String temperature = "36" + "." + RandomUtil.randomInt(1, 5);
        String location = user.getLocation() == null ? DEFAULT_LOCATION : user.getLocation();
        Map<String, Object> bodyMap = new HashMap<String, Object>();
        if (code == MORNING_CLICK_CODE) {
            bodyMap.put("24[0][0][name]", "form[24][field_1588749561_2922][]");bodyMap.put("24[0][0][value]", temperature);
            bodyMap.put("24[0][1][name]", "form[24][field_1588749738_1026][]");bodyMap.put("24[0][1][value]", location);
            bodyMap.put("24[0][2][name]", "form[24][field_1588749759_6865][]");bodyMap.put("24[0][2][value]", "是");
            bodyMap.put("24[0][3][name]", "form[24][field_1588749842_2715][]");bodyMap.put("24[0][3][value]", "否");
            bodyMap.put("24[0][4][name]", "form[24][field_1588749886_2103][]");bodyMap.put("24[0][4][value]", "1");
        } else if (code == NOON_CLICK_CODE) {
            bodyMap.put("25[0][0][name]","form[25][field_1588750276_2934][]");bodyMap.put("25[0][0][value]", temperature);
            bodyMap.put("25[0][1][name]","form[25][field_1588750304_5363][]");bodyMap.put("25[0][1][value]", location);
            bodyMap.put("25[0][2][name]","form[25][field_1588750323_2500][]");bodyMap.put("25[0][2][value]", "是");
            bodyMap.put("25[0][3][name]","form[25][field_1588750343_3510][]");bodyMap.put("25[0][3][value]", "否");
            bodyMap.put("25[0][4][name]","form[25][field_1588750363_5268][]");bodyMap.put("25[0][4][value]", "1");
        }
        Map<String, Object> encodeBodyMap = new HashMap<String, Object>();
        for (String key : bodyMap.keySet()) {
            String v = (String) bodyMap.get(key);
            encodeBodyMap.put(new String(key.getBytes(), "UTF-8"), new String(v.getBytes(), "UTF-8"));
        }
        return encodeBodyMap;
    }

    private static String getClickRequestUrl(int code) {
        if (code == 13) {
            return "http://yiban.sust.edu.cn/v4/public/index.php/Index/formflow/add.html?desgin_id=13&list_id=9";
        }
        return String.format("http://yiban.sust.edu.cn/v4/public/index.php/Index/formflow/add.html?desgin_id=%s&list_id=12", code);
    }


    public static String getCookie(User user) {
        try {
            String url = user.getUrl();
            HttpResponse resp = HttpRequest
                    .post(url)
                    //.header(Header.COOKIE, "")
                    .disableCookie()
                    .execute();
            String cookie = resp.getCookieStr().split(";")[0].split("=")[1];
            return cookie;
        } catch (Exception e) {
            return null;
        }
    }


    private static void handleOutput(String output) {
        JSONObject jsonObject = JSONUtil.parseObj(output);
        String userStr = jsonObject.get("user").toString();
        User user = JSONUtil.toBean(userStr, User.class);
        Integer statusCode = (Integer) jsonObject.get("status_code");
        String statusMsg = (String) jsonObject.get("status_msg");

        //全部打卡成功
        if (statusCode == null) {
            String msg = String.format("all clicks success, time is {%s}, id is {%s}, name is {%s}", DateUtil.now(), user.getId(), user.getName());
            logger.info(msg);
        } else if (statusCode == ExceptionStatus.NOT_CONSUME) {
            //无需打卡
            logger.info(statusMsg);
        } else {
            //其他打卡异常
            logger.error(statusMsg);
        }
    }

    private static void sendEmail(String output, User user) {
        Email email = getEmail(output, user);
        if (email != null) {
            addUrlDateWarn(email, user);
            EmailUtil.send(email);
        }
    }

    //添加url使用过长的报警
    private static void addUrlDateWarn(Email email, User user) {
        Date generateDate = user.getUrlGenerateDate();
        Date now = DateUtil.date();
        if (generateDate == null) {
            return ;
        }
        long day = DateUtil.betweenDay(generateDate, now, true);
        if (day >= DEFAULT_URL_WARN_DAY) {
            String content = email.getContent();
            content += user.getName() + ", 你的url已经使用了" + day + "天, " + "已经超过了" +DEFAULT_URL_WARN_DAY + "天。以免url失效，请及时更新url！";
            email.setContent(content);
        }
    }

    private static Email getEmail(String output, User user) {
        JSONObject jsonObject = JSONUtil.parseObj(output);
        String email = user.getEmail();
        String title = "易班打卡全部成功！";
        String content = user.getName() + ", 恭喜你，易班打卡全部成功。\n\r";
        Integer status_code = (Integer) jsonObject.get("status_code");
        String statusMsg = (String) jsonObject.get("status_msg");
        //打卡无错误，全部成功
        if (status_code == null) {
            content = emailAddTime(output, content);
            return new Email(email, title, content);
        }
        //账号配置无打卡，无需发邮件
        if (status_code == ExceptionStatus.NOT_CONSUME) {
            return null;
        }
        //打卡中途异常
        if (status_code != null && status_code == ExceptionStatus.CONNECT_FAIL || status_code == ExceptionStatus.UNKNOWN_ERROR) {
            title = "易班打卡出错，请检查配置/联系作者！";
            content = user.getName() + ", 易班打卡出错，请检查配置/联系作者！\n\r";
            content += "错误信息: " + statusMsg + "\r\n";
        }
        //打卡全部失败/部分失败
        if (status_code != null && status_code == ExceptionStatus.CLICK_ALL_FAIL) {
            title = "易班打卡全部失败，请检查配置！";
            content = user.getName() + ", 易班打卡全部失败，请检查配置！\n\r";
            content += "错误信息: " + statusMsg + "\r\n";
        }
        if (status_code != null && status_code == ExceptionStatus.CLICK_PART_FAIL) {
            title = "易班打卡部分失败，失败打卡是";
            Boolean hasMorningClick =  (Boolean) jsonObject.get("has_morning_click");
            Boolean hasNoonClick =  (Boolean) jsonObject.get("has_noon_click");
            if (user.getMorningClick() == true && hasMorningClick == false)
                title += "晨检打卡！";
            else if (user.getNoonClick() == true && hasNoonClick == false)
                title += "午检打卡!";
            content = user.getName() + ", 易班打卡部分失败\n\r";
            content += "错误信息: " + statusMsg + "\r\n";
        }
        content = emailAddTime(output, content);
        return new Email(email, title, content);
    }

    private static String emailAddTime(String output, String content) {
        JSONObject jsonObject = JSONUtil.parseObj(output);
        String startTime = (String) jsonObject.get("start_time");
        String endTime = (String) jsonObject.get("end_time");
        content += "开始时间: " + startTime + "\r\n";
        content += "结束时间: " + endTime + "\r\n";
        return content;
    }

    public static void main(String[] args) {
        //初始化
        init();
        //对每一个用户打卡
        for (User user : users) {
            String startTime = DateUtil.now();
            JSONObject res = click(user);
            String endTime = DateUtil.now();
            res.put("start_time", startTime);
            res.put("end_time", endTime);
            //处理输出
            handleOutput(res.toString());
            System.out.println(res.toString());
            //发送邮件
            if (user.getSendEmail() == true && user.getEmail() != null)
                sendEmail(res.toString(), user);
        }
    }
}
