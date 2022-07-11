package com.study.totee.api.service;


import com.study.totee.api.dto.category.CategoryRequestDto;
import com.study.totee.api.dto.category.CategoryUpdateDto;
import com.study.totee.api.model.CategoryEntity;
import com.study.totee.api.persistence.CategoryRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
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
        // 카테고리 이름 중복 체크
        categoryRepository.findByCategoryName(categoryRequestDto.getCategoryName())
                .ifPresent(m-> {
                    throw new BadRequestException(ErrorCode.ALREADY_EXIST_CATEGORY_ERROR);
                });
        CategoryEntity category = CategoryEntity.builder().categoryName(categoryRequestDto.getCategoryName())
                .build();
        // 카테고리 요청 dto 에 이미지가 있으면 s3에 이미지 업로드, 없으면 null 처리
        if(categoryRequestDto.getCategoryImage() != null){
            category.setImageUrl(awsS3Service.upload(categoryRequestDto.getCategoryImage(), "static"));
        }
        categoryRepository.save(category);
    }

    @Transactional
    public void delete(CategoryRequestDto categoryRequestDto){
        CategoryEntity category = categoryRepository.findByCategoryName(categoryRequestDto.getCategoryName()).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_CATEGORY_ERROR));
        // 카테고리 entity 에 이미지가 있으면 s3에서 이미지 삭제
        if(category.getImageUrl() != null){
            awsS3Service.fileDelete(category.getImageUrl());
        }
        categoryRepository.delete(category);
    }

    @Transactional
    public void update(CategoryUpdateDto categoryUpdateDto) throws IOException {
        CategoryEntity category = categoryRepository.findByCategoryName(categoryUpdateDto.getCategoryName()).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_CATEGORY_ERROR));
        // 기존 카테고리 이름과 변경 할 카테고리 이름이 같지않으면 업데이트 한다.
        if(!categoryUpdateDto.getNewCategoryName().equals(category.getCategoryName())){
            category.setCategoryName(categoryUpdateDto.getNewCategoryName());
        }
        // 카테고리 요청 dto 에 이미지가 있으면 s3에 이미지 업로드, 없으면 null 처리
        if(categoryUpdateDto.getCategoryImage() != null){
            // 기존 이미지가 있으면 s3에서 이미지 삭제
            if(category.getImageUrl() != null){
                awsS3Service.fileDelete(category.getImageUrl());
            }
            // 새로운 이미지 업로드
            category.setImageUrl(awsS3Service.upload(categoryUpdateDto.getCategoryImage(), "static"));
        }
    }

    @Transactional(readOnly = true)
    public List<CategoryEntity> categoryEntityList(){
        return categoryRepository.findAll();
    }

}
