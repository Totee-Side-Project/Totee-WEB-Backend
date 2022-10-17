//package com.study.totee.cateogory;
//
//import com.study.totee.api.dto.category.CategoryRequestDto;
//import com.study.totee.api.dto.category.CategoryResponseDto;
//import com.study.totee.api.dto.category.CategoryUpdateRequestDto;
//import com.study.totee.api.model.Category;
//import com.study.totee.api.persistence.CategoryQueryRepository;
//import com.study.totee.api.persistence.CategoryRepository;
//import com.study.totee.api.service.CategoryService;
//import com.study.totee.exption.BadRequestException;
//import com.study.totee.exption.ErrorCode;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//import static com.study.totee.util.EntityFactory.categoryRequestDto;
//import static com.study.totee.util.EntityFactory.categoryUpdateRequestDto;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class CategoryServiceTest {
//
//    @InjectMocks
//    private CategoryService target;
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @Mock
//    private CategoryQueryRepository categoryQueryRepository;
//
//    @Test
//    public void 카테고리등록성공() throws IOException {
//        // given
//        CategoryRequestDto dto = categoryRequestDto(1);
//        given(categoryRepository.save(any(Category.class))).willReturn(new Category(dto));
//
//        // when
//        final CategoryResponseDto result = target.save(dto);
//
//        // then
//        assertThat(result.getCategoryName()).isEqualTo("프로젝트1");
//
//        //verify
//        verify(categoryQueryRepository, times(1)).findByCategoryName("프로젝트1");
//        verify(categoryRepository, times(1)).save(any(Category.class));
//    }
//
//    @Test
//    public void 카테고리등록실패_중복이름(){
//        // given
//        CategoryRequestDto dto = categoryRequestDto(1);
//        given(categoryQueryRepository.findByCategoryName(dto.getCategoryName())).willReturn(new Category());
//
//        // when
//        final BadRequestException result = assertThrows(BadRequestException.class, () -> target.save(dto));
//
//        // then
//        assertThat(result.getMessage()).isEqualTo(ErrorCode.ALREADY_EXIST_CATEGORY_ERROR.getMessage());
//    }
//
//    @Test
//    public void 카테고리목록조회성공() {
//
//        // given
//        List<Category> categories = Arrays.asList(new Category(), new Category(), new Category());
//        given(categoryQueryRepository.findAll()).willReturn(categories);
//
//        // when
//        final List<CategoryResponseDto> result = target.categoryResponseDtoList();
//
//        // then
//        assertThat(result.size()).isEqualTo(3);
//    }
//
//    @Test
//    public void 카테고리수정실패_존재하지않음(){
//        // given
//        CategoryUpdateRequestDto dto = categoryUpdateRequestDto(1);
//        given(categoryQueryRepository.findByCategoryName(dto.getCategoryName())).willReturn(null);
//
//        // when
//        final BadRequestException result = assertThrows(BadRequestException.class, () -> target.update(dto));
//
//        // then
//        assertThat(result.getMessage()).isEqualTo(ErrorCode.NOT_EXIST_CATEGORY_ERROR.getMessage());
//    }
//
//    @Test
//    public void 카테고리수정실패_중복이름(){
//        // given
//        CategoryRequestDto dto1 = categoryRequestDto(1);
//        CategoryUpdateRequestDto dto2 = CategoryUpdateRequestDto.builder().categoryName("프로젝트1")
//                        .newCategoryName("프로젝트1").build();
//        given(categoryQueryRepository.findByCategoryName(dto2.getNewCategoryName())).willReturn(new Category(dto1));
//
//        // when
//        final BadRequestException result = assertThrows(BadRequestException.class, () -> target.update(dto2));
//
//        // then
//        assertThat(result.getMessage()).isEqualTo(ErrorCode.ALREADY_EXIST_CATEGORY_ERROR.getMessage());
//    }
//
//    @Test
//    public void 카테고리수정성공() throws IOException {
//        // given
//        CategoryRequestDto requestDto = categoryRequestDto(1);
//        CategoryUpdateRequestDto updateRequestDto = categoryUpdateRequestDto(1);
//        given(categoryQueryRepository.findByCategoryName(any(String.class))).willReturn(new Category(requestDto));
//
//        // when
//        CategoryResponseDto result = target.update(updateRequestDto);
//
//        // then
//        assertThat(result.getCategoryName()).isEqualTo("프로젝트1수정");
//
//        // verify
//        verify(categoryQueryRepository, times(1)).findByCategoryName(updateRequestDto.getCategoryName());
//    }
//
//    @Test
//    public void 카테고리삭제실패_존재하지않음() {
//        // given
//        CategoryRequestDto dto = categoryRequestDto(1);
//        given(categoryQueryRepository.findByCategoryName(dto.getCategoryName())).willReturn(null);
//
//        // when
//        final BadRequestException result = assertThrows(BadRequestException.class, () -> target.delete(dto));
//
//        // then
//        assertThat(result.getMessage()).isEqualTo(ErrorCode.NOT_EXIST_CATEGORY_ERROR.getMessage());
//    }
//
//    @Test
//    public void 카테고리삭제성공(){
//        // given
//        CategoryRequestDto dto = categoryRequestDto(1);
//        given(categoryQueryRepository.findByCategoryName(dto.getCategoryName())).willReturn(new Category(dto));
//        // when
//        target.delete(dto);
//        // then
//        verify(categoryRepository, times(1)).delete(any(Category.class));
//        verify(categoryQueryRepository, times(1)).findByCategoryName(dto.getCategoryName());
//    }
//}
