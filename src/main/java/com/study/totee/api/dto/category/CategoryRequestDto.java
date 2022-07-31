package com.study.totee.api.dto.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CategoryRequestDto {
    @ApiModelProperty(example = "카테고리 이름")
    private String categoryName;
}
