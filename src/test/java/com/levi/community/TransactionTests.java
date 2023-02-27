package com.levi.community;

import com.levi.community.service.AlphaService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransactionTests {

    @Autowired
    private AlphaService alphaService;


    @Test
    void testSave1() {
        Object o = alphaService.save1();
        System.out.println(o);
    }

    @Test
    void testSave2() {
        Object o = alphaService.save2();
        System.out.println(o);
    }
}
