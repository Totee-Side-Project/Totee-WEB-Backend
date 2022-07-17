package com.study.totee.api.dto.reply;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyRequestDto {
    @ApiModelProperty(example = "댓글 번호")
    private Long commentId;

    @ApiModelProperty(example = "내용")
    private String content;
}
