package com.study.totee.api.service;

import com.study.totee.api.dto.category.CategoryRequestDto;
import com.study.totee.api.dto.category.CategoryResponseDto;
import com.study.totee.api.model.CategoryEntity;
import com.study.totee.api.persistence.CategoryRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService target;

    @Mock
    private CategoryRepository categoryRepository;

    private final Long id = 1L;
    private final String categoryName = "categoryName";

    @Test
    public void 카테고리등록실패_이미존재(){
        // given
        doReturn(CategoryEntity.builder().build()).when(categoryRepository).findByCategoryName(categoryName);
        CategoryRequestDto dto = CategoryRequestDto.builder().categoryName(categoryName).build();

        // when
        final BadRequestException result = assertThrows(BadRequestException.class, () -> target.save(dto));

        // then
        assertThat(result.getMessage()).isEqualTo(ErrorCode.ALREADY_EXIST_CATEGORY_ERROR.getMessage());
    }

    @Test
    public void 카테고리등록성공() throws IOException {
        // given
        doReturn(null).when(categoryRepository).findByCategoryName(categoryName);
        doReturn(category()).when(categoryRepository).save(any(CategoryEntity.class));

        // when
        CategoryRequestDto dto = CategoryRequestDto.builder().categoryName(categoryName).build();
        final CategoryResponseDto result = target.save(dto);

        // then
        assertThat(result.getCategoryName()).isNotNull();

        //verify
        verify(categoryRepository, times(1)).findByCategoryName(categoryName);
        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    }

    private CategoryEntity category(){
        return CategoryEntity.builder().categoryId(-1L).categoryName(categoryName).build();
    }
}
