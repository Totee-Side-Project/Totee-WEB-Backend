package com.study.totee.api.dto.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@Getter
@NoArgsConstructor
public class CommentRequestDto {
    @ApiModelProperty(example = "게시글 번호")
    private Long postId;

    @ApiModelProperty(example = "내용")
    private String content;
}
