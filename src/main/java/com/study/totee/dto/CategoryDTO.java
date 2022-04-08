package com.study.totee.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    @ApiModelProperty(example = "카테고리 이름")
    private String categoryName;
}
