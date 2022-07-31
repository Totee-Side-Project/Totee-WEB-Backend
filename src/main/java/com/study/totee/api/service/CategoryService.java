package com.study.totee.api.service;


import com.study.totee.api.dto.category.CategoryRequestDto;
import com.study.totee.api.dto.category.CategoryResponseDto;
import com.study.totee.api.dto.category.CategoryUpdateDto;
import com.study.totee.api.model.CategoryEntity;
import com.study.totee.api.persistence.CategoryRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponseDto save(CategoryRequestDto categoryRequestDto) throws IOException {
        final CategoryEntity result = categoryRepository.findByCategoryName(categoryRequestDto.getCategoryName());
        if (result != null) {
            throw new BadRequestException(ErrorCode.ALREADY_EXIST_CATEGORY_ERROR);
        }

        final CategoryEntity category = CategoryEntity.builder().categoryName(categoryRequestDto.getCategoryName())
                .build();

        final CategoryEntity savedCategoryEntity = categoryRepository.save(category);

        return CategoryResponseDto.builder().categoryName(savedCategoryEntity.getCategoryName()).build();
    }

    @Transactional
    public void update(CategoryUpdateDto categoryUpdateDto) throws IOException {
        CategoryEntity category = categoryRepository.findByCategoryName(categoryUpdateDto.getCategoryName());
        if (category == null) {
            throw new BadRequestException(ErrorCode.NOT_EXIST_CATEGORY_ERROR);
        }
        // 기존 카테고리 이름과 변경 할 카테고리 이름이 같지않으면 업데이트 한다.
        if(categoryUpdateDto.getNewCategoryName().equals(category.getCategoryName())){
            throw new BadRequestException(ErrorCode.BAD_REQUEST_ERROR);
        }
        category.setCategoryName(categoryUpdateDto.getNewCategoryName());
    }

    @Transactional
    public void delete(CategoryRequestDto categoryRequestDto){
        CategoryEntity category = categoryRepository.findByCategoryName(categoryRequestDto.getCategoryName());
        if (category == null) {
            throw new BadRequestException(ErrorCode.NOT_EXIST_CATEGORY_ERROR);
        }
        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> categoryEntityList(){

        final List<CategoryEntity> categoryEntityList = categoryRepository.findAll();

        return categoryEntityList.stream()
                .map(categoryEntity -> CategoryResponseDto.builder()
                        .categoryName(categoryEntity.getCategoryName())
                        .build())
                .collect(Collectors.toList());
    }

}
