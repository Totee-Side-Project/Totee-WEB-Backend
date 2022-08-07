//package com.study.totee.api.controller;
//
//import com.google.gson.Gson;
//
//import com.study.totee.api.dto.category.CategoryRequestDto;
//import com.study.totee.api.dto.category.CategoryResponseDto;
//import com.study.totee.api.dto.category.CategoryUpdateDto;
//import com.study.totee.api.service.CategoryService;
//import com.study.totee.exption.BadRequestException;
//import com.study.totee.exption.ErrorCode;
//import com.study.totee.handler.RestApiExceptionHandler;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Arrays;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@Slf4j
//@ExtendWith(MockitoExtension.class)
//public class CategoryControllerTest {
//
//    @InjectMocks
//    private CategoryController target;
//
//    @Mock
//    private CategoryService categoryService;
//
//    private MockMvc mockMvc;
//    private Gson gson;
//
//    private final String categoryName = "categoryName";
//
//    @BeforeEach
//    public void init() {
//        gson = new Gson();
//        mockMvc = MockMvcBuilders.standaloneSetup(target)
//                .setControllerAdvice(new RestApiExceptionHandler())
//                .build();
//    }
//
//    @Test
//    public void 카테고리등록성공() throws Exception{
//        // given
//        final String url = "/api/v1/category";
//
//        CategoryResponseDto responseDto = CategoryResponseDto.builder().categoryName(categoryName).build();
//        CategoryRequestDto requestDto = CategoryRequestDto.builder().categoryName(categoryName).build();
//
//        given(categoryService.save(any(CategoryRequestDto.class))).willReturn(responseDto);
//
//        // when
//        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
//                    .post(url)
//                    .content(gson.toJson(requestDto))
//                    .contentType(MediaType.APPLICATION_JSON_UTF8));
//
//        // then
//        resultActions.andExpect(status().isCreated());
//
//        final CategoryResponseDto result = gson.fromJson(resultActions.andReturn()
//            .getResponse().getContentAsString(), CategoryResponseDto.class);
//
//        assertThat(result.getCategoryName()).isEqualTo(categoryName);
//    }
//
//    @Test
//    public void 카테고리등록실패_CategoryService에서에러Throw() throws Exception{
//        // given
//        final String url = "/api/v1/category";
//
//        CategoryRequestDto requestDto = CategoryRequestDto.builder().categoryName(categoryName).build();
//
//        doThrow(new BadRequestException(ErrorCode.ALREADY_EXIST_CATEGORY_ERROR))
//                .when(categoryService).save(requestDto);
//
//        // when
//        final ResultActions resultActions = mockMvc.perform(
//                MockMvcRequestBuilders.post(url)
//                        .content(gson.toJson(requestDto))
//                        .contentType(MediaType.APPLICATION_JSON_UTF8));
//        // then
//        resultActions.andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void 카테고리목록조회성공() throws Exception{
//        // given
//        final String url = "/api/v1/category";
//
//        doReturn(Arrays.asList(CategoryResponseDto.builder().build(),
//                CategoryResponseDto.builder().build(),
//                CategoryResponseDto.builder().build()
//        )).when(categoryService).categoryResponseDtoList();
//
//        // when
//        final ResultActions resultActions = mockMvc.perform(
//                MockMvcRequestBuilders.get(url));
//
//        // then
//        resultActions.andExpect(status().isOk());
//    }
//
//    @Test
//    public void 카테고리삭제성공() throws Exception{
//        // given
//        CategoryRequestDto requestDto = CategoryRequestDto.builder().categoryName(categoryName).build();
//        final String url = "/api/v1/category";
//
//        // when
//        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
//                .delete(url)
//                .content(gson.toJson(requestDto))
//                .contentType(MediaType.APPLICATION_JSON_UTF8));
//
//        // then
//        resultActions.andExpect(status().isNoContent());
//    }
//
//    @Test
//    public void 카테고리삭제실패_CategoryService에서에러Throw() throws Exception{
//        // given
//        CategoryRequestDto requestDto = CategoryRequestDto.builder().categoryName(categoryName).build();
//        final String url = "/api/v1/category";
//
//        lenient().doThrow(new BadRequestException(ErrorCode.NOT_EXIST_CATEGORY_ERROR))
//                .when(categoryService).delete(requestDto);
//
//        // when
//        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
//                .delete(url));
//
//        // then
//        resultActions.andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void 카테고리수정실패_존재하지않음() throws Exception {
//        // given
//        CategoryUpdateDto requestDto = CategoryUpdateDto.builder().categoryName("null").newCategoryName("null").build();
//        final String url = "/api/v1/category";
//
//        lenient().doThrow(new BadRequestException(ErrorCode.NOT_EXIST_CATEGORY_ERROR))
//                .when(categoryService).update(requestDto);
//
//        // when
//        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
//                .put(url)
//                .content(gson.toJson(requestDto))
//                .contentType(MediaType.APPLICATION_JSON_UTF8));
//
//        // then
//        resultActions.andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void 카테고리수정실패_같은이름으로변경() throws Exception{
//        // given
//        CategoryUpdateDto requestDto = CategoryUpdateDto.builder().categoryName("null").newCategoryName("null").build();
//        final String url = "/api/v1/category";
//
//        lenient().doThrow(new BadRequestException(ErrorCode.ALREADY_EXIST_CATEGORY_ERROR))
//                .when(categoryService).update(requestDto);
//
//        // when
//        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
//                .put(url)
//                .content(gson.toJson(requestDto))
//                .contentType(MediaType.APPLICATION_JSON_UTF8));
//
//        // then
//        resultActions.andExpect(status().isBadRequest());
//
//    }
//}
