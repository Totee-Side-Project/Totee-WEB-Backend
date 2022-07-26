package com.study.totee.api.controller;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryControllerTest {

    @InjectMocks
    private CategoryController target;

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .build();
    }

    @Test
    public void mockMvc가Null이아님() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .build();
        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
    }
}
