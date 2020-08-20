package com.agile;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 佟盟
 * 日期 2020/8/00018 17:30
 * 描述 TODO
 * @version 1.0
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Test {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @org.junit.Test
    public void test1() {
        testRestTemplate.getForObject("/test1?a=12", String.class);
    }

    @org.junit.Test
    public void test2() {
        testRestTemplate.getForObject("/test2?a=12", String.class);
    }
}
