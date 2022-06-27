package com.study.totee.api.dto.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateDto {
    @ApiModelProperty(example = "기존 카테고리 이름")
    private String categoryName;

    @ApiModelProperty(example = "변경할 카테고리 이름")
    private String newCategoryName;

    @ApiModelProperty(example = "바꿀 카테고리 이미지")
    private MultipartFile categoryImage;
}
