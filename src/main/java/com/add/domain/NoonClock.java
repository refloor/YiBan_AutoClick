package com.add.domain;

import cn.hutool.core.util.RandomUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class NoonClock extends Clock{
    String name = "易班午检";
    String startTime = "12:00:00";
    String endTime = "15:00:00";
    int code = 25;
    String reqUrl = String.format("http://yiban.sust.edu.cn/v4/public/index.php/Index/formflow/add.html?desgin_id=%s&list_id=12", code);



    @Override
    Map<String, Object> getClockRequestBody(User user) throws UnsupportedEncodingException {
        String temperature = "36" + "." + RandomUtil.randomInt(1, 5);
        String location = user.getLocation() == null ? DEFAULT_LOCATION : user.getLocation();
        Map<String, Object> bodyMap = new HashMap<String, Object>();
        bodyMap.put("25[0][0][name]","form[25][field_1588750276_2934][]");bodyMap.put("25[0][0][value]", temperature);
        bodyMap.put("25[0][1][name]","form[25][field_1588750304_5363][]");bodyMap.put("25[0][1][value]", location);
        bodyMap.put("25[0][2][name]","form[25][field_1588750323_2500][]");bodyMap.put("25[0][2][value]", "是");
        bodyMap.put("25[0][3][name]","form[25][field_1588750343_3510][]");bodyMap.put("25[0][3][value]", "否");
        bodyMap.put("25[0][4][name]","form[25][field_1588750363_5268][]");bodyMap.put("25[0][4][value]", "1");
        Map<String, Object> encodeBodyMap = new HashMap<String, Object>();
        for (String key : bodyMap.keySet()) {
            String v = (String) bodyMap.get(key);
            encodeBodyMap.put(new String(key.getBytes(), "UTF-8"), new String(v.getBytes(), "UTF-8"));
        }
        return encodeBodyMap;
    }

    @Override
    String getClockRequestUrl() {
        return this.reqUrl;
    }






}
