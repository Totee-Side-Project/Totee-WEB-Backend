package com.study.totee.cateogory;

import com.study.totee.api.model.Category;
import com.study.totee.api.persistence.CategoryRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;


import javax.persistence.EntityManager;

import static com.study.totee.util.EntityFactory.categoryRequestDto;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Order(1)
    @Rollback(false)
    public void 카테고리_등록() {
        // given
        final Category categoryEntity = new Category(categoryRequestDto(1));

        // when
        final Category savedCategoryEntity = categoryRepository.save(categoryEntity);

        // then
        assertThat(savedCategoryEntity.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void 카테고리_조회() {
        // given

        // when
        final Category foundCategoryEntity = categoryRepository.findById(1L).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_CATEGORY_ERROR));

        // then
        assertThat(foundCategoryEntity.getId()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    @Rollback(false)
    public void 카테고리_수정(){
        // given
        final Category foundCategoryEntity = categoryRepository.findById(1L).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_CATEGORY_ERROR));

        // when
        foundCategoryEntity.setCategoryName("수정테스트");

        // then
        assertThat(categoryRepository.findByCategoryName("수정테스트")).isNotNull();
    }

    @Test
    @Order(4)
    @Rollback(false)
    public void 카테고리_삭제(){
        // given
        final Category foundCategoryEntity = categoryRepository.findById(1L).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_CATEGORY_ERROR));

        // when
        categoryRepository.delete(foundCategoryEntity);

        // then
        assertThat(categoryRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Order(5)
    @Rollback(false)
    public void 초기화(){
        categoryRepository.deleteAll();

        entityManager.createNativeQuery("ALTER TABLE tb_category ALTER COLUMN `category_id` RESTART WITH 1")
                .executeUpdate();
    }
}