package com.study.totee.api.dto.mentor;

import com.study.totee.api.model.Mentor;
import com.study.totee.api.model.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class MentorResponseDto {
    @ApiModelProperty(example = "닉네임")
    private String nickname;

    @ApiModelProperty(example = "이메일")
    private String email;

    @ApiModelProperty(example = "분야")
    private String field;

    @ApiModelProperty(example = "경력")
    private String career;

    @ApiModelProperty(example = "개인 연락처")
    private String contact;

    @ApiModelProperty(example = "개인 포트폴리오")
    private String portfolioUrl;

    @ApiModelProperty(example = "자기소개 코멘트")
    private String comment;

    @ApiModelProperty(example = "멘토 지원 승인 여부")
    private boolean approval;

    @ApiModelProperty(example = "프로필 이미지")
    private String profileImageUrl;

    public MentorResponseDto(Mentor mentor){
        User user = mentor.getUser();
        this.nickname = user.getUserInfo().getNickname();
        this.email = user.getEmail();
        this.field = mentor.getField();
        this.career = mentor.getCareer();
        this.contact = mentor.getContact();
        this.portfolioUrl = mentor.getPortfolioUrl();
        this.comment = mentor.getComment();
        this.approval = mentor.getApproval().equals("approved");
        this.profileImageUrl = user.getUserInfo().getProfileImageUrl();
    }
}
