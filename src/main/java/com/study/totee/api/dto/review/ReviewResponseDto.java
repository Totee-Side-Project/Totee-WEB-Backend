package com.study.totee.api.dto.review;

import com.study.totee.api.model.Mentoring;
import com.study.totee.api.model.Review;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class ReviewResponseDto {
    @ApiModelProperty(example = "스코어")
    private float score;

    @ApiModelProperty(example = "후기 코멘트")
    private String comment;

    public ReviewResponseDto(Review review){
        this.score = review.getScore();
        this.comment = review.getComment();
    }
}
