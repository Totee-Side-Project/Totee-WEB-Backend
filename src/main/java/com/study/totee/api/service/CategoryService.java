package com.study.totee.api.service;


import com.study.totee.api.dto.category.CategoryRequestDto;
import com.study.totee.api.dto.category.CategoryResponseDto;
import com.study.totee.api.dto.category.CategoryUpdateRequestDto;
import com.study.totee.api.model.Category;
import com.study.totee.api.persistence.CategoryQueryRepository;
import com.study.totee.api.persistence.CategoryRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryQueryRepository categoryQueryRepository;

    @Transactional
    public CategoryResponseDto save(CategoryRequestDto categoryRequestDto) throws IOException {
        // 카테고리 이름이 중복되는지 확인합니다.
        final Category result = categoryQueryRepository.findByCategoryName(categoryRequestDto.getCategoryName());

        if (result != null) {
            throw new BadRequestException(ErrorCode.ALREADY_EXIST_CATEGORY_ERROR);
        }

        final Category savedCategoryEntity = categoryRepository.save(new Category(categoryRequestDto));

        return new CategoryResponseDto(savedCategoryEntity);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> categoryResponseDtoList(){
        // 저장된 카테고리 Entity 리스트를 모두 조회하여 Dto 리스트로 변환한 후 반환합니다.

        return categoryQueryRepository.findAll().stream()
                .map(CategoryResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponseDto update(CategoryUpdateRequestDto categoryUpdateRequestDto) throws IOException {
        // 수정할 카테고리가 존재하는지 확인합니다.
        Category category = categoryQueryRepository.findByCategoryName(categoryUpdateRequestDto.getCategoryName());
        if (category == null) {
            throw new BadRequestException(ErrorCode.NOT_EXIST_CATEGORY_ERROR);
        }

        // 기존 카테고리 이름과 변경 할 카테고리 이름이 같으면 예외를 던집니다.
        if(categoryUpdateRequestDto.getNewCategoryName().equals(category.getCategoryName())){
            throw new BadRequestException(ErrorCode.ALREADY_EXIST_CATEGORY_ERROR);
        }

        category.setCategoryName(categoryUpdateRequestDto.getNewCategoryName());
        return new CategoryResponseDto(category);
    }

    @Transactional
    public CategoryResponseDto delete(CategoryRequestDto categoryRequestDto){
        // 삭제할 카테고리가 존재하는지 확인합니다.
        Category category = categoryQueryRepository.findByCategoryName(categoryRequestDto.getCategoryName());

        if (category == null) {
            throw new BadRequestException(ErrorCode.NOT_EXIST_CATEGORY_ERROR);
        }

        categoryRepository.delete(category);
        return new CategoryResponseDto(category);
    }

}
