package com.study.totee.controller;

import com.study.totee.dto.ApiResponse;
import com.study.totee.dto.CategoryDTO;
import com.study.totee.model.CategoryEntity;
import com.study.totee.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation(value = "카테고리 목록" , notes = "카테고리 목록 보기 ")
    @GetMapping("api/v1/category")
    public ApiResponse<List<CategoryEntity>> categoryList(){
        return ApiResponse.success("data", categoryService.categoryEntityList());
    }

    @ApiOperation(value = "카테고리 추가" , notes = "카테고리 넣기 ")
    @PostMapping("api/v1/category/add")
    public ApiResponse addCategory(@RequestBody CategoryDTO categoryDTO) throws IOException {
        CategoryEntity categoryEntity = CategoryEntity.builder().categoryName(categoryDTO.getCategoryName()).build();
        categoryService.save(categoryEntity);
        return ApiResponse.success("message", "Success");
    }

    @ApiOperation(value = "카테고리 삭제" , notes = "카테고리 삭제, 포스트가 해당 카테고리를 사용하고 있지 않아야 합니다.")
    @DeleteMapping("api/v1/category/delete")
    public ApiResponse deleteCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.delete(categoryDTO);
        return ApiResponse.success("message", "Success");
    }

    @ApiOperation(value = "카테고리 업데이트" , notes = "카테고리를 업데이트 업데이트를 할 경우 포스트가 자동으로 변경 된 값을 참조함.")
    @PutMapping("api/v1/category/update")
    public ApiResponse updateCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);

        return ApiResponse.success("message", "Success");
    }
}
