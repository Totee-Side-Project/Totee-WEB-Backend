package com.study.totee.api.dto.comment;

import com.study.totee.api.dto.reply.ReplyResponseDto;
import com.study.totee.api.model.Comment;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Getter
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
    private LocalDateTime createdAt;

    public CommentResponseDto(Comment comment){
        this.nickname = comment.getUser().getUserInfo().getNickname();
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.profileImageUrl = comment.getUser().getUserInfo().getProfileImageUrl();
        this.replyList = comment.getReply().stream().map(ReplyResponseDto::new).collect(Collectors.toList());
        this.createdAt = comment.getCreatedAt();
    }
}
