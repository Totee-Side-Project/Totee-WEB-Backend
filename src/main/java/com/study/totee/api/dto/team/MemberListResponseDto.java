package com.study.totee.api.dto.team;

import com.study.totee.api.model.Applicant;
import com.study.totee.api.model.Team;
import com.study.totee.api.model.User;
import com.study.totee.type.PositionType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Getter
@NoArgsConstructor
public class MemberListResponseDto {

    @ApiModelProperty(example = "닉네임")
    private String nickname;

    @ApiModelProperty(example = "지원메세지")
    private String message;

    @ApiModelProperty(example = "지원 날짜")
    private LocalDateTime applicationDate;

    @ApiModelProperty(example = "이메일")
    private String email;

    @ApiModelProperty(example = "프로필 이미지")
    private String profileImg;

    @ApiModelProperty(example = "분야")
    private PositionType position;

    public MemberListResponseDto(Team team) {
        User user = team.getUser();
        this.nickname = user.getUserInfo().getNickname();
        this.applicationDate = team.getCreatedAt();
        this.email = user.getEmail();
        this.profileImg = user.getUserInfo().getProfileImageUrl();
        this.position = user.getUserInfo().getPosition();
    }

    public MemberListResponseDto(Applicant applicant) {
        User user = applicant.getUser();
        this.nickname = user.getUserInfo().getNickname();
        this.applicationDate = applicant.getCreatedAt();
        this.email = user.getEmail();
        this.profileImg = user.getUserInfo().getProfileImageUrl();
        this.message = applicant.getMessage();
        this.position = user.getUserInfo().getPosition();
    }
}
