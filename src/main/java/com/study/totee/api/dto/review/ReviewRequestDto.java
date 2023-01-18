package com.study.totee.api.dto.review;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class ReviewRequestDto {
    @ApiModelProperty(value = "3.5")
    private float score;

    @ApiModelProperty(example = "멘토링 좋았다")
    private String comment;
}
