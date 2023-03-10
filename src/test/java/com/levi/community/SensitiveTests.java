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
        String text = "θΏιε―δ»₯πθ΅πεποΌε―δ»₯πε«πε¨ΌποΌε―δ»₯πεΈπζ―ποΌε―δ»₯πεΌπη₯¨ποΌεεεοΌ";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }
}
