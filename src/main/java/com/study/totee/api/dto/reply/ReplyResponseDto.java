package com.study.totee.api.dto.reply;

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
public class ReplyResponseDto {
    @ApiModelProperty(example = "작성자")
    private String username;

    @ApiModelProperty(example = "대댓글 번호")
    private Long replyId;

    @ApiModelProperty(example = "내용")
    private String content;

    @ApiModelProperty(example = "작성 날짜")
    private LocalDateTime created_at;
}
