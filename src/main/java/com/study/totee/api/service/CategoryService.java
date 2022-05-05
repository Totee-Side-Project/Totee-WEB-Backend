package com.study.totee.api.service;

import com.study.totee.api.dto.CategoryDTO;
import com.study.totee.api.model.CategoryEntity;
import com.study.totee.api.persistence.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void save(CategoryEntity category) {
        validateDuplicateCategoryName(category.getCategoryName());
        categoryRepository.save(category);
    }

    @Transactional
    public void delete(CategoryDTO categoryDTO){
        CategoryEntity category = categoryRepository.findByCategoryName(categoryDTO.getCategoryName()).orElseThrow(
                ()-> new IllegalArgumentException("찾을 수 없는 카테고리 입니다."));
        categoryRepository.deleteByCategoryName(category.getCategoryName());
    }

    @Transactional
    public void update(CategoryDTO categoryDTO){
        CategoryEntity category = categoryRepository.findByCategoryName(categoryDTO.getCategoryName()).orElseThrow(
                ()-> new IllegalArgumentException("찾을 수 없는 카테고리 입니다."));

        validateDuplicateCategoryName(categoryDTO.getCategoryName());
        category.setCategoryName(categoryDTO.getCategoryName());
    }

    @Transactional(readOnly = true)
    public List<CategoryEntity> categoryEntityList(){
        return categoryRepository.findAll();
    }

    public Optional<CategoryEntity> findByCategoryName(String name){
        return categoryRepository.findByCategoryName(name);
    }

    private void validateDuplicateCategoryName(String categoryName) {
        categoryRepository.findByCategoryName(categoryName)
                .ifPresent(m-> {
                    throw new IllegalStateException("이미 존재하는 카테고리 입니다.");
                });
    }
}
