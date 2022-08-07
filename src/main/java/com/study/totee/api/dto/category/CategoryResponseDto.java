package com.study.totee.api.dto.category;

import com.study.totee.api.model.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor
public class CategoryResponseDto {
    @ApiModelProperty(example = "카테고리 이름")
    private String categoryName;

    public CategoryResponseDto(Category category) {
        this.categoryName = category.getCategoryName();
    }
}
