package com.study.totee.cateogory;

import com.study.totee.api.dto.category.CategoryRequestDto;
import com.study.totee.api.dto.category.CategoryResponseDto;
import com.study.totee.api.model.Category;
import com.study.totee.api.persistence.CategoryQueryRepository;
import com.study.totee.api.persistence.CategoryRepository;
import com.study.totee.api.service.CategoryService;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.study.totee.api.util.EntityFactory.categoryRequestDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService target;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryQueryRepository categoryQueryRepository;

    @Test
    public void 카테고리등록성공() throws IOException {
        // given
        CategoryRequestDto dto = categoryRequestDto(1);
        given(categoryRepository.save(any(Category.class))).willReturn(new Category(dto));

        // when
        final CategoryResponseDto result = target.save(dto);

        // then
        assertThat(result.getCategoryName()).isEqualTo("프로젝트1");

        //verify
        verify(categoryQueryRepository, times(1)).findByCategoryName("프로젝트1");
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void 카테고리등록실패_중복이름() throws IOException {
        // given
        CategoryRequestDto dto = categoryRequestDto(1);
        given(categoryQueryRepository.findByCategoryName("프로젝트1")).willReturn(any(Category.class));

        // when
        final BadRequestException result = assertThrows(BadRequestException.class, () -> target.save(dto));

        // then
        assertThat(result.getMessage()).isEqualTo(ErrorCode.ALREADY_EXIST_CATEGORY_ERROR.getMessage());

        //verify
        verify(categoryRepository, times(1)).save(any(Category.class));
    }
//
//    @Test
//    public void 카테고리등록실패_이미존재(){
//        // given
//        doReturn(Category.builder().build()).when(categoryRepository).findByCategoryName("TEST");
//        CategoryRequestDto dto = CategoryRequestDto.builder().categoryName("TEST").build();
//
//        // when
//        final BadRequestException result = assertThrows(BadRequestException.class, () -> target.save(dto));
//
//        // then
//        assertThat(result.getMessage()).isEqualTo(ErrorCode.ALREADY_EXIST_CATEGORY_ERROR.getMessage());
//    }
//
//    @Test
//    public void 카테고리목록조회() {
//        // given
//        doReturn(Arrays.asList(
//                category(),
//                category(),
//                category()
//        )).when(categoryRepository).findAll();
//
//        // when
//        final List<CategoryResponseDto> result = target.categoryResponseDtoList();
//
//        // then
//        assertThat(result.size()).isEqualTo(3);
//    }
//
//    @Test
//    public void 카테고리삭제실패_존재하지않음(){
//        // given
//        lenient().doReturn(null).when(categoryRepository).findByCategoryName("categoryName");
//        CategoryRequestDto dto = CategoryRequestDto.builder().categoryName("TEST").build();
//
//        // when
//        final BadRequestException result = assertThrows(BadRequestException.class, () -> target.delete(dto));
//
//        // then
//        assertThat(result.getMessage()).isEqualTo(ErrorCode.NOT_EXIST_CATEGORY_ERROR.getMessage());
//    }
//
//
//
//    @Test
//    public void 카테고리삭제성공() {
//        // given
//        final Category categoryEntity = category();
//        doReturn(categoryEntity).when(categoryRepository).findByCategoryName("TEST");
//
//        // when
//        target.delete(CategoryRequestDto.builder().categoryName("TEST").build());
//    }
//
//    @Test
//    public void 카테고리수정실패_존재하지않음() {
//        // given
//        CategoryUpdateDto dto = CategoryUpdateDto.builder().categoryName("TEST").newCategoryName("TEST").build();
//
//        // when
//        final BadRequestException result = assertThrows(BadRequestException.class, () -> target.update(dto));
//
//        // then
//        assertThat(result.getMessage()).isEqualTo(ErrorCode.NOT_EXIST_CATEGORY_ERROR.getMessage());
//
//    }
//
//    @Test
//    public void 카테고리수정실패_같은이름으로변경() {
//        // given
//        final Category categoryEntity = category();
//        doReturn(categoryEntity).when(categoryRepository).findByCategoryName("TEST");
//        CategoryUpdateDto dto = CategoryUpdateDto.builder().categoryName("TEST").newCategoryName("TEST").build();
//        // when
//        final BadRequestException result = assertThrows(BadRequestException.class, () -> target.update(dto));
//
//        // then
//        assertThat(result.getMessage()).isEqualTo(ErrorCode.BAD_REQUEST_ERROR.getMessage());
//    }
}
