package com.study.totee.api.service;


import com.study.totee.api.dto.category.CategoryRequestDto;
import com.study.totee.api.dto.category.CategoryUpdateDto;
import com.study.totee.api.model.CategoryEntity;
import com.study.totee.api.persistence.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AwsS3Service awsS3Service;

    @Transactional
    public void save(CategoryRequestDto categoryRequestDto) throws IOException {
        validateDuplicateCategoryName(categoryRequestDto.getCategoryName());
        CategoryEntity category = CategoryEntity.builder().categoryName(categoryRequestDto.getCategoryName())
                .build();
        if(categoryRequestDto.getCategoryImage() != null){
            category.setImageUrl(awsS3Service.upload(categoryRequestDto.getCategoryImage(), "static"));
        }
        categoryRepository.save(category);
    }

    @Transactional
    public void delete(CategoryRequestDto categoryRequestDto){
        CategoryEntity category = categoryRepository.findByCategoryName(categoryRequestDto.getCategoryName()).orElseThrow(
                ()-> new IllegalArgumentException("찾을 수 없는 카테고리 입니다."));
        if(category.getImageUrl() != null){
            awsS3Service.fileDelete(category.getImageUrl());
        }
        categoryRepository.delete(category);
    }

    @Transactional
    public void update(CategoryUpdateDto categoryUpdateDto) throws IOException {
        CategoryEntity category = categoryRepository.findByCategoryName(categoryUpdateDto.getCategoryName()).orElseThrow(
                ()-> new IllegalArgumentException("찾을 수 없는 카테고리 입니다."));
        if(!categoryUpdateDto.getNewCategoryName().equals(category.getCategoryName())){
            validateDuplicateCategoryName(categoryUpdateDto.getNewCategoryName());
            category.setCategoryName(categoryUpdateDto.getNewCategoryName());
        }

        if(categoryUpdateDto.getCategoryImage() != null){
            if(category.getImageUrl() != null){
                awsS3Service.fileDelete(category.getImageUrl());
            }
            category.setImageUrl(awsS3Service.upload(categoryUpdateDto.getCategoryImage(), "static"));
        }
    }

    @Transactional(readOnly = true)
    public List<CategoryEntity> categoryEntityList(){
        return categoryRepository.findAll();
    }

    private void validateDuplicateCategoryName(String categoryName) {
        categoryRepository.findByCategoryName(categoryName)
                .ifPresent(m-> {
                    throw new IllegalStateException("이미 존재하는 카테고리 입니다.");
                });
    }
}
