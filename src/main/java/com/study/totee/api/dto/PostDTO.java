package com.study.totee.api.dto;

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
public class PostDTO {
    @ApiModelProperty(example = "작성자, 기입X")
    private String username;
    @ApiModelProperty(example = "조회수, 기입X")
    private int view;
    @ApiModelProperty(example = "포스트번호, 기입X")
    private Long postId;
    @ApiModelProperty(example = "작성날짜, 기입X")
    private LocalDateTime createdAt;
    @ApiModelProperty(example = "전공, 기입X")
    private String major;
    @ApiModelProperty(example = "제목")
    private String title;
    @ApiModelProperty(example = "내용")
    private String content;
    @ApiModelProperty(example = "카테고리")
    private String categoryName;
    @ApiModelProperty(example = "좋아요 수, 기입X")
    private int likeCount;
    @ApiModelProperty(example = "모집 상태 (Y or N), 수정시 만 기입")
    private String status;
    @ApiModelProperty(example = "댓글 수, 기입X")
    private int commentCount;
    @ApiModelProperty(example = "댓글리스트 정보, 기입X")
    private List<CommentDTO> commentDTOList;
}
