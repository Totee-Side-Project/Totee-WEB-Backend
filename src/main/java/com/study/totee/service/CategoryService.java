package com.study.totee.service;

import com.study.totee.dto.CategoryDTO;
import com.study.totee.model.CategoryEntity;
import com.study.totee.model.PostEntity;
import com.study.totee.persistence.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void save(CategoryEntity categoryEntity) throws IOException {
        categoryRepository.save(categoryEntity);
    }

    @Transactional
    public void delete(CategoryEntity category){
        categoryRepository.delete(category);
    }

    @Transactional
    public void update(CategoryDTO categoryDTO){
        CategoryEntity category = categoryRepository.findByCategoryName(categoryDTO.getCategoryName()).orElseThrow(
                ()-> new IllegalArgumentException("찾을 수 없는 카테고리 입니다."));

        validateDuplicateCategoryName(categoryDTO.getCategoryName());
        category.setCategoryName(categoryDTO.getCategoryName());
    }

    private void validateDuplicateCategoryName(String categoryName) {
        categoryRepository.findByCategoryName(categoryName)
                .ifPresent(m-> {
                    throw new IllegalStateException("이미 존재하는 카테고리 입니다.");
                });
    }
}
