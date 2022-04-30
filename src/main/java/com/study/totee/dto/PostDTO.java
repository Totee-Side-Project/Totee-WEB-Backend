package com.study.totee.dto;

import com.study.totee.model.CommentEntity;
import com.study.totee.model.LikeEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
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
    @ApiModelProperty(example = "제목")
    private String title;
    @ApiModelProperty(example = "내용")
    private String content;
    @ApiModelProperty(example = "썸네일 소개")
    private String intro;
    @ApiModelProperty(example = "카테고리")
    private String categoryName;
    @ApiModelProperty(example = "좋아요 수")
    private int likeCount;
    @ApiModelProperty(example = "댓글 수")
    private int commentCount;
    @ApiModelProperty(example = "댓글리스트 정보")
    private List<CommentDTO> commentDTOList;
}
