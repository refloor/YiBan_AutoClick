package com.add.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.mail.MailUtil;
import com.add.domain.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class EmailUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);


    //给指定email发送邮件
    public static void send(String email, String title, String content) {
        try {
            MailUtil.send(email, title, content, false);
            String msg = String.format("success to send email, email is {%s}, title is {%s}, content is {%s}", email, title, content);
            logger.info(msg);
        } catch (Exception e) {
            String msg = String.format("fail to send email, time is {%s}, email is {%s}, title is {%s}, content is {%s}", DateUtil.now(),email, title, content);
            logger.error(msg, e);
        }
    }

    //给指定email发邮件
    public static void send(Email email) {
        send(email.getEmail(), email.getTitle(), email.getContent());
    }
}
