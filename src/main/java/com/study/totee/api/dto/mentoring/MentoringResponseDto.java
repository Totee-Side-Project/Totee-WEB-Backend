package com.study.totee.api.dto.mentoring;

import com.study.totee.api.model.Mentor;
import com.study.totee.api.model.Mentoring;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class MentoringResponseDto {

    @ApiModelProperty(example = "멘토링 게시글 번호")
    private Long mentoringId;
    @ApiModelProperty(example = "제목")
    private String title;

    @ApiModelProperty(example = "내용")
    private String content;

    @ApiModelProperty(example = "1회 비용")
    private int cost;

    @ApiModelProperty(example = "분야")
    private String field;

    @ApiModelProperty(example = "경력")
    private String career;

    @ApiModelProperty(example = "닉네임")
    private String nickname;

    @ApiModelProperty(example = "프로필 이미지 Url")
    private String profileImageUrl;

    @ApiModelProperty(example = "리뷰 스코어")
    private String score;

    public MentoringResponseDto(Mentoring mentoring){
        this.mentoringId = mentoring.getId();
        this.title = mentoring.getTitle();
        this.content = mentoring.getContent();
        this.cost = mentoring.getCost();
        this.field = mentoring.getUser().getMentor().getField();
        this.career = mentoring.getUser().getMentor().getCareer();
        this.nickname = mentoring.getUser().getUserInfo().getNickname();
        this.profileImageUrl = mentoring.getUser().getUserInfo().getProfileImageUrl();
        this.score = mentoring.getReviewScore() == 0 ? null :
                String.format("%.1f", mentoring.getReviewScore() / mentoring.getReviewList().size());
    }
}