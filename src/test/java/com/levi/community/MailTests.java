package com.levi.community;

import com.levi.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
public class MailTests {
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;

    @Test
    void testTextMail() {
        mailClient.sendMail("1625094334@qq.com", "TEST", "hello");
    }

    @Test
    void testHTMLMail() {
        Context context = new Context();
        context.setVariable("username", "friday");
        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);
        mailClient.sendMail("1625094334@qq.com", "HTML", content);
    }
}
