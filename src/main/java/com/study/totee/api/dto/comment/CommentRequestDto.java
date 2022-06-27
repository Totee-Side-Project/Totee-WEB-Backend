package com.study.totee.api.dto.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    @ApiModelProperty(example = "게시글 번호")
    private Long postId;

    @ApiModelProperty(example = "내용")
    private String content;
}
