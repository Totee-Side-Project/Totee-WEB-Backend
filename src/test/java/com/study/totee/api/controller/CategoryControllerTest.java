package com.study.totee.api.controller;

import com.google.gson.Gson;
import com.study.totee.api.dto.category.CategoryRequestDto;
import com.study.totee.handler.RestApiExceptionHandler;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryControllerTest {

    @InjectMocks
    private CategoryController target;

    private MockMvc mockMvc;
    private Gson gson;

    private final String categoryName = "categoryName";

    @BeforeEach
    public void init() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new RestApiExceptionHandler())
                .build();
    }

    @Test
    public void mockMvc가Null이아님() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .build();
        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    public void 카테고리등록실패_사용자식별값이헤더에없음() throws Exception{
        // given
        final String url = "/api/v1/category";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(categoryRequestDto(categoryName)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    private CategoryRequestDto categoryRequestDto(final String categoryName) {
        return CategoryRequestDto.builder().categoryName(categoryName).build();
    }
}
