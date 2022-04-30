package com.study.totee.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    @ApiModelProperty(example = "작성자, 기입X")
    private String username;

    @ApiModelProperty(example = "댓글 번호, 기입X")
    private Long commentId;

    @ApiModelProperty(example = "게시글 번호, 기입X")
    private Long postId;

    @ApiModelProperty(example = "내용")
    private String content;

    @ApiModelProperty(example = "작성 날짜, 기입X")
    private LocalDateTime created_at;
}
