package com.study.totee.api.dto.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Getter
@NoArgsConstructor
public class CategoryRequestDto {

    @ApiModelProperty(example = "카테고리 이름")
    private String categoryName;

    @Builder
    public CategoryRequestDto(String categoryName) {
        this.categoryName = categoryName;
    }
}
