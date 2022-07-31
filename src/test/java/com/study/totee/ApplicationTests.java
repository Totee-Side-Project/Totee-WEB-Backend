package com.study.totee;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
properties = "spring.config.location=classpath:application.yml")
public class ApplicationTests {

    @Test
    public void contextLoads() {
    }
}
