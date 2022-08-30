package com.study.totee.cateogory;

import com.google.gson.Gson;
import com.study.totee.api.controller.CategoryController;
import com.study.totee.api.dto.category.CategoryRequestDto;
import com.study.totee.api.dto.category.CategoryResponseDto;
import com.study.totee.api.dto.category.CategoryUpdateRequestDto;
import com.study.totee.api.model.Category;
import com.study.totee.api.service.CategoryService;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import com.study.totee.handler.RestApiExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static com.study.totee.util.EntityFactory.categoryUpdateRequestDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.study.totee.util.EntityFactory.categoryRequestDto;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @InjectMocks
    private CategoryController target;

    @Mock
    private CategoryService categoryService;

    private MockMvc mockMvc;
    private Gson gson;

    private final String url = "/api/v1/category";

    private final CategoryRequestDto requestDto = categoryRequestDto(1);
    private final CategoryUpdateRequestDto updateRequestDto = categoryUpdateRequestDto(1);
    private final Category category = new Category(requestDto);
    private final CategoryResponseDto responseDto = new CategoryResponseDto(category);

    @BeforeEach
    public void init() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new RestApiExceptionHandler())
                .build();
    }

    @Test
    public void 카테고리등록실패_CategoryService에서에러Throw() throws Exception{
        // given
        given(categoryService.save(any(CategoryRequestDto.class)))
                .willThrow(new BadRequestException(ErrorCode.ALREADY_EXIST_CATEGORY_ERROR));

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 카테고리등록성공() throws Exception{
        // given
        given(categoryService.save(any(CategoryRequestDto.class))).willReturn(responseDto);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                    .post(url)
                    .content(gson.toJson(requestDto)).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated());

        final CategoryResponseDto result = gson.fromJson(resultActions.andReturn()
            .getResponse().getContentAsString(StandardCharsets.UTF_8), CategoryResponseDto.class);

        assertThat(result.getCategoryName()).isEqualTo(requestDto.getCategoryName());
    }


    @Test
    public void 카테고리목록조회성공() throws Exception{
        // given
        List<CategoryResponseDto> categories = Arrays.asList(new CategoryResponseDto(category),
                new CategoryResponseDto(category), new CategoryResponseDto(category));

        given(categoryService.categoryResponseDtoList()).willReturn(categories);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.body.data.length()", is(3)));
    }

    @Test
    public void 카테고리수정실패_CategoryService에서에러Throw() throws Exception {
        // given
        given(categoryService.update(any(CategoryUpdateRequestDto.class)))
                .willThrow(new BadRequestException(ErrorCode.NOT_EXIST_CATEGORY_ERROR));
        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(url)
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 카테고리수정성공() throws Exception {
        // given

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(url)
                        .content(gson.toJson(updateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void 카테고리삭제실패_CategoryService에서에러Throw() throws Exception{
        // given
        given(categoryService.delete(any(CategoryRequestDto.class)))
                .willThrow(new BadRequestException(ErrorCode.NOT_EXIST_CATEGORY_ERROR));

        // when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(url)
                .content(gson.toJson(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 카테고리삭제성공() throws Exception{
        // given

        // when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(url)
                .content(gson.toJson(requestDto))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
    }
}
