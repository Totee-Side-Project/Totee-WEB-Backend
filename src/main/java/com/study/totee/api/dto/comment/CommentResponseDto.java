package com.study.totee.api.dto.comment;

import com.study.totee.api.dto.reply.ReplyResponseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    @ApiModelProperty(example = "작성자")
    private String nickname;

    @ApiModelProperty(example = "댓글 번호")
    private Long commentId;

    @ApiModelProperty(example = "내용")
    private String content;

    @ApiModelProperty(example = "작성자 프로필 URL")
    private String profileImageUrl;

    @ApiModelProperty(example = "대댓글 리스트")
    private List<ReplyResponseDto> replyList;

    @ApiModelProperty(example = "작성 날짜")
    private LocalDateTime created_at;
}
