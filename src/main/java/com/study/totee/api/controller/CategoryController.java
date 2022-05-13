package com.study.totee.api.controller;


import com.study.totee.common.ApiResponse;
import com.study.totee.api.dto.CategoryDTO;
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
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @ApiOperation(value = "카테고리 목록" , notes = "카테고리 목록 보기")
    @GetMapping("api/v1/category")
    public ApiResponse categoryList(){
        List<CategoryEntity> entityList = categoryService.categoryEntityList();
        List<CategoryDTO> dtoList = Arrays.asList(modelMapper.map(entityList, CategoryDTO[].class));
        return ApiResponse.success("data", dtoList);
    }

    @ApiOperation(value = "카테고리 추가" , notes = "카테고리 넣기")
    @PostMapping("api/v1/category/add")
    public ApiResponse addCategory(@ModelAttribute @Valid @RequestBody CategoryDTO categoryDTO) throws IOException {
        CategoryEntity categoryEntity = CategoryEntity.builder().categoryName(categoryDTO.getCategoryName()).build();
        categoryService.save(categoryEntity, categoryDTO.getCategoryImage());
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
    public ApiResponse updateCategory(@RequestBody CategoryDTO categoryDTO) throws IOException {
        categoryService.update(categoryDTO);

        return ApiResponse.success("message", "Success");
    }
}
