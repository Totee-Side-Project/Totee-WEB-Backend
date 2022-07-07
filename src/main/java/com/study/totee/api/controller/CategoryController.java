package com.study.totee.api.controller;


import com.study.totee.api.dto.category.CategoryRequestDto;
import com.study.totee.api.dto.category.CategoryResponseDto;
import com.study.totee.api.dto.category.CategoryUpdateDto;
import com.study.totee.common.ApiResponse;
import com.study.totee.api.model.CategoryEntity;
import com.study.totee.api.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @ApiOperation(value = "카테고리 목록" , notes = "카테고리 목록 보기")
    @GetMapping("/api/v1/category")
    public ApiResponse categoryList(){
        List<CategoryEntity> entityList = categoryService.categoryEntityList();
        List<CategoryResponseDto> dtoList = Arrays.asList(modelMapper.map(entityList, CategoryResponseDto[].class));
        return ApiResponse.success("data", dtoList);
    }

    @ApiOperation(value = "카테고리 추가" , notes = "카테고리 추가, 폼데이터 형식으로 보내야됩니다. 필수!")
    @PostMapping("/api/v1/category")
    public ApiResponse addCategory(@ModelAttribute @Valid @RequestBody CategoryRequestDto categoryRequestDto) throws IOException {
        categoryService.save(categoryRequestDto);
        return ApiResponse.success("message", "카테고리가 성공적으로 추가되었습니다.");
    }

    @ApiOperation(value = "카테고리 삭제" , notes = "카테고리 삭제, 포스트가 해당 카테고리를 사용하고 있지 않아야 합니다.")
    @DeleteMapping("/api/v1/category")
    public ApiResponse deleteCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        categoryService.delete(categoryRequestDto);
        return ApiResponse.success("message", "카테고리가 성공적으로 삭제되었습니다.");
    }

    @ApiOperation(value = "카테고리 업데이트" , notes = "카테고리 업데이트, 업데이트를 할 경우 포스트가 자동으로 변경 된 값을 참조함.")
    @PutMapping("/api/v1/category")
    public ApiResponse updateCategory(@ModelAttribute @Valid @RequestBody CategoryUpdateDto categoryUpdateDto) throws IOException {
        categoryService.update(categoryUpdateDto);

        return ApiResponse.success("message", "카테고리가 성공적으로 업데이트되었습니다.");
    }
}