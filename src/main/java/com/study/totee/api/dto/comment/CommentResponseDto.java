package com.study.totee.api.dto.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    @ApiModelProperty(example = "작성자")
    private String nickname;

    @ApiModelProperty(example = "댓글 번호")
    private Long commentId;

    @ApiModelProperty(example = "작성 날짜")
    private LocalDateTime created_at;
}
