package com.add;

import cn.hutool.extra.mail.MailUtil;
import com.add.exception.BaseException;
import com.add.util.EmailUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MailTest {

    private static final Logger logger = LoggerFactory.getLogger(MailTest.class);

    @Test
    public void test1() {
        String email = "324234";
        String title = "测试";
        String content = "邮件来自yb_clock测试";
        EmailUtil.send(email, title, content);
    }

    @Test
    public void test2() {
        String email = "1111";
        String title = "测试";
        String content = "邮件来自yb_clock测试";
        String msg = String.format("success to send email, email is {%s}, title is {%s}, content is {%s}", email, title, content);
        logger.info(msg);
    }

}
