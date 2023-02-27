package com.levi.community;

import com.levi.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SensitiveTests {
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    void testSensitiveFilter() {
        String text = "这里可以😊赌😊博😊，可以😊嫖😊娼😊，可以😊吸😊毒😊，可以😊开😊票😊，哈哈哈！";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }
}
