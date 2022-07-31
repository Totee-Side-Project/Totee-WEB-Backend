package com.study.totee.api.persistence;

import com.study.totee.api.model.CategoryEntity;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CategoryRepository 테스트 케이스
 *
 * @author Marine
 * @since 2022.07.31
 */

@DataJpaTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void 카테고리_등록() {
        // given
        final CategoryEntity categoryEntity = CategoryEntity.builder()
                .categoryName("TEST")
                .build();

        // when
        final CategoryEntity savedCategoryEntity = categoryRepository.save(categoryEntity);

        // then
        assertThat(savedCategoryEntity.getCategoryId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void 카테고리_조회() {
        // given

        // when
        final CategoryEntity foundCategoryEntity = categoryRepository.findByCategoryName("TEST");

        // then
        assertThat(foundCategoryEntity.getCategoryName()).isEqualTo("TEST");
        assertThat(categoryRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    public void 카테고리_수정(){
        // given
        final CategoryEntity foundCategoryEntity = categoryRepository.findByCategoryName("TEST");

        // when
        foundCategoryEntity.setCategoryName("TEST_UPDATE");

        // then
        assertThat(categoryRepository.findByCategoryName("TEST_UPDATE")).isNotNull();
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void 카테고리_삭제(){
        // given
        final CategoryEntity foundCategoryEntity = categoryRepository.findByCategoryName("TEST_UPDATE");

        // when
        categoryRepository.delete(foundCategoryEntity);

        // then
        assertThat(categoryRepository.findByCategoryName("TEST_UPDATE")).isNull();
        assertThat(categoryRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    public void 초기화() {
        this.categoryRepository.deleteAll();

        this.entityManager
                .createNativeQuery("ALTER TABLE tb_category ALTER COLUMN `category_id` RESTART WITH 1")
                .executeUpdate();
    }
}

