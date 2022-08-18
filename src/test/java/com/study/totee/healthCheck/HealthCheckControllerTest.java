package com.study.totee.healthCheck;

import com.google.gson.Gson;
import com.study.totee.api.controller.HealthCheckController;
import com.study.totee.handler.RestApiExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class HealthCheckControllerTest {

    @InjectMocks
    private HealthCheckController target;

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    public void init() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new RestApiExceptionHandler())
                .build();
    }

    @Test
    public void HealthCheck() throws Exception {
        String message = "The service is up and running..";
        // MockMvc를 통해 / 주소로 HTTP GET 요청
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/"));

        resultActions.andExpect(status().isOk());
    }
}