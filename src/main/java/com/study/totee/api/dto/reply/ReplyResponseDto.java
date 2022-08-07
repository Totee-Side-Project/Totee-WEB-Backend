package com.study.totee.api.dto.reply;

import com.study.totee.api.model.Reply;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class ReplyResponseDto {
    @ApiModelProperty(example = "작성자")
    private String nickname;

    @ApiModelProperty(example = "대댓글 번호")
    private Long replyId;

    @ApiModelProperty(example = "작성자 프로필 URL")
    private String profileImageUrl;

    @ApiModelProperty(example = "내용")
    private String content;

    @ApiModelProperty(example = "작성 날짜")
    private LocalDateTime createdAt;

    public ReplyResponseDto(Reply reply){
        this.nickname = reply.getUser().getUserInfo().getNickname();
        this.replyId = reply.getId();
        this.profileImageUrl = reply.getUser().getUserInfo().getProfileImageUrl();
        this.content = reply.getContent();
        this.createdAt = reply.getCreatedAt();
    }
}
