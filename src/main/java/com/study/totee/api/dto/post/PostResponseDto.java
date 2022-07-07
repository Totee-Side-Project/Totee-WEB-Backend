package com.study.totee.api.dto.post;

import com.study.totee.api.dto.comment.CommentResponseDto;
import com.study.totee.type.PositionType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {

    @ApiModelProperty(example = "포스트번호")
    private Long postId;

    @ApiModelProperty(example = "제목")
    private String title;

    @ApiModelProperty(example = "내용")
    private String content;

    @ApiModelProperty(example = "작성자")
    private String author;

    @ApiModelProperty(example = "조회수")
    private int view;

    @ApiModelProperty(example = "좋아요 수")
    private int likeNum;

    @ApiModelProperty(example = "댓글 수")
    private int commentNum;

    @ApiModelProperty(example = "댓글리스트 정보")
    private List<CommentResponseDto> commentDTOList;

    @ApiModelProperty(example = "포스트 이미지 URL")
    private String imageUrl;

    @ApiModelProperty(example = "작성날짜")
    private LocalDateTime createdAt;

    @ApiModelProperty(name = "미팅 방식 (ON or OFF)")
    private String onlineOrOffline;

    @ApiModelProperty(name = "예상 기간 (월 단위)")
    private int period;

    @ApiModelProperty(example = "모집 상태 (Y or N)")
    private String status;

    @ApiModelProperty(example = "카테고리")
    private String categoryName;

    @ApiModelProperty(example = "모집 대상 포지션 리스트 (ex Designer, Front_End..)")
    private List<String> positionList;

    @ApiModelProperty(example = "모집 인원 수")
    private int recruitNum;

    @ApiModelProperty(example = "연락 방법")
    private String contactMethod;

    @ApiModelProperty(example = "연락 링크")
    private String contactLink;

    @ApiModelProperty(example = "작성자 포지션")
    private PositionType authorPosition;
}
