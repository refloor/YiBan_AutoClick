package com.add.domain;

import cn.hutool.core.util.RandomUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class MorningClock extends Clock{
    String name = "易班晨检";
    String startTime = "06:00:00";
    String endTime = "09:00:00";
    int code = 24;
    String reqUrl = String.format("http://yiban.sust.edu.cn/v4/public/index.php/Index/formflow/add.html?desgin_id=%s&list_id=12", code);



    @Override
    Map<String, Object> getClockRequestBody(User user) throws UnsupportedEncodingException {
        String temperature = "36" + "." + RandomUtil.randomInt(1, 5);
        String location = user.getLocation() == null ? DEFAULT_LOCATION : user.getLocation();
        Map<String, Object> bodyMap = new HashMap<String, Object>();
        bodyMap.put("24[0][0][name]", "form[24][field_1588749561_2922][]");bodyMap.put("24[0][0][value]", temperature);
        bodyMap.put("24[0][1][name]", "form[24][field_1588749738_1026][]");bodyMap.put("24[0][1][value]", location);
        bodyMap.put("24[0][2][name]", "form[24][field_1588749759_6865][]");bodyMap.put("24[0][2][value]", "是");
        bodyMap.put("24[0][3][name]", "form[24][field_1588749842_2715][]");bodyMap.put("24[0][3][value]", "否");
        bodyMap.put("24[0][4][name]", "form[24][field_1588749886_2103][]");bodyMap.put("24[0][4][value]", "1");
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
