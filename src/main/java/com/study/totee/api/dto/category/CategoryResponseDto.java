package com.study.totee.api.dto.category;

import com.study.totee.api.model.CategoryEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDto {
    @ApiModelProperty(example = "카테고리 이름")
    private String categoryName;

//    public CategoryResponseDto(CategoryEntity category){
//        this.categoryName = category.getCategoryName();
//    }
}
