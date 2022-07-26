package com.study.totee.healthCheck.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HealthCheckControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void HealthCheck() throws Exception {
        String message = "The service is up and running..";
        // MockMvc를 통해 / 주소로 HTTP GET 요청
        mockMvc.perform(get("/"))
                // mvc.perform의 결과를 검증1 - HTTP Header의 Status 검증 (Ok이므로 200인지 검증)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(message));
    }
}