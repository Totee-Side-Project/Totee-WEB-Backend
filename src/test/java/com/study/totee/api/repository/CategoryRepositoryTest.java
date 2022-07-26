package com.study.totee.api.repository;

import com.study.totee.api.model.CategoryEntity;
import com.study.totee.api.persistence.CategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void categoryRepository가Null이아님() {
        assertThat(categoryRepository).isNotNull();
    }

    @Test
    public void 카테고리_등록() {
        // given
        final CategoryEntity categoryEntity = CategoryEntity.builder()
                .categoryId(1L)
                .categoryName("카테고리1")
                .build();

        // when
        final CategoryEntity savedCategoryEntity = categoryRepository.save(categoryEntity);

        // then
        assertThat(savedCategoryEntity.getCategoryId()).isNotNull();
        assertThat(savedCategoryEntity.getCategoryName()).isEqualTo("카테고리1");
    }

    @Test
    public void 카테고리_조회() {
        // given
        final CategoryEntity categoryEntity = CategoryEntity.builder()
                .categoryId(1L)
                .categoryName("카테고리1")
                .build();

        // when
        CategoryEntity savedCategoryEntity = categoryRepository.save(categoryEntity);
        final CategoryEntity foundCategoryEntity = categoryRepository.findByCategoryName(savedCategoryEntity.getCategoryName());

        // then
        assertThat(foundCategoryEntity.getCategoryId()).isNotNull();
        assertThat(foundCategoryEntity.getCategoryName()).isEqualTo("카테고리1");
    }
}
