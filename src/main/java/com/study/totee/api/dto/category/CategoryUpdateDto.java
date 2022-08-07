package com.study.totee.api.dto.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@NoArgsConstructor
public class CategoryUpdateDto {
    @ApiModelProperty(example = "기존 카테고리 이름")
    private String categoryName;

    @ApiModelProperty(example = "변경할 카테고리 이름")
    private String newCategoryName;
}
