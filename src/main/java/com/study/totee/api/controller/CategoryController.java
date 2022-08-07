package com.study.totee.api.controller;


import com.study.totee.api.dto.category.CategoryRequestDto;
import com.study.totee.api.dto.category.CategoryResponseDto;
import com.study.totee.api.dto.category.CategoryUpdateDto;
import com.study.totee.common.ApiResponse;
import com.study.totee.api.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation(value = "카테고리 추가" , notes = "카테고리 추가")
    @PostMapping("/api/v1/category")
    public ResponseEntity<Object> addCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) throws IOException {
        CategoryResponseDto categoryResponseDto = categoryService.save(categoryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponseDto);
    }

    @ApiOperation(value = "카테고리 목록" , notes = "카테고리 목록 보기")
    @GetMapping("/api/v1/category")
    public ApiResponse<Object> categoryList(){
        List<CategoryResponseDto> dtoList = categoryService.categoryResponseDtoList();
        return ApiResponse.success("data", dtoList);
    }

    @ApiOperation(value = "카테고리 업데이트" , notes = "카테고리 업데이트, 업데이트를 할 경우 포스트가 자동으로 변경 된 값을 참조함.")
    @PutMapping("/api/v1/category")
    public ResponseEntity<Object> updateCategory(@Valid @RequestBody CategoryUpdateDto categoryUpdateDto) throws IOException {
        CategoryResponseDto categoryResponseDto = categoryService.update(categoryUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDto);
    }

    @ApiOperation(value = "카테고리 삭제" , notes = "카테고리 삭제, 포스트가 해당 카테고리를 사용하고 있지 않아야 합니다.")
    @DeleteMapping("/api/v1/category")
    public ResponseEntity<Object> deleteCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto categoryResponseDto = categoryService.delete(categoryRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDto);
    }
}