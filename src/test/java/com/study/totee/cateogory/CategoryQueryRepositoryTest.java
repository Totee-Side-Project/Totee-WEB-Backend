package com.study.totee.cateogory;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.totee.api.dto.category.CategoryRequestDto;
import com.study.totee.api.model.Category;
import com.study.totee.api.persistence.CategoryQueryRepository;
import com.study.totee.api.persistence.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class CategoryQueryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryQueryRepository categoryQueryRepository;


    @Test
    public void 카테고리_이름조회() {
        //given
        String name = "프로젝트";

        CategoryRequestDto categoryRequestDto = new CategoryRequestDto(name);

        categoryRepository.save(new Category(categoryRequestDto));

        //when
        Category result = categoryQueryRepository.findByCategoryName(name);

        //then
        assertThat(result.getCategoryName()).isEqualTo(name);
    }

    @Test
    public void 카테고리_전체조회() {
        //given
        String name = "프로젝트";
        String name2 = "업무";
        String name3 = "기타";
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto(name);
        CategoryRequestDto categoryRequestDto2 = new CategoryRequestDto(name2);
        CategoryRequestDto categoryRequestDto3 = new CategoryRequestDto(name3);
        categoryRepository.save(new Category(categoryRequestDto));
        categoryRepository.save(new Category(categoryRequestDto2));
        categoryRepository.save(new Category(categoryRequestDto3));
        //when
        Iterable<Category> result = categoryQueryRepository.findAll();
        //then
        assertThat(result).hasSize(3);
    }

}
