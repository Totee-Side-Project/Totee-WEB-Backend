package com.study.totee.api.dto.team;

import com.study.totee.api.model.MentoringApplicant;
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
public class MenteeListResponseDto {

    @ApiModelProperty(example = "닉네임")
    private String nickname;

    @ApiModelProperty(example = "자기소개")
    private String comment;

    @ApiModelProperty(example = "연락 방법")
    private String contact;

    @ApiModelProperty(example = "시작 시간")
    private String startTime;

    @ApiModelProperty(example = "종료 시간")
    private String endTime;

    @ApiModelProperty(example = "지원 날짜")
    private LocalDateTime applicationDate;

    @ApiModelProperty(example = "이메일")
    private String email;

    @ApiModelProperty(example = "프로필 이미지")
    private String profileImg;

    @ApiModelProperty(example = "분야")
    private PositionType position;

    @ApiModelProperty(example = "희망요일")
    private String week;

    public MenteeListResponseDto(MentoringApplicant applicant){
        this.nickname = applicant.getUser().getUserInfo().getNickname();
        this.comment = applicant.getComment();
        this.contact = applicant.getContact();
        this.startTime = applicant.getStartTime();
        this.endTime = applicant.getEndTime();
        this.applicationDate = applicant.getCreatedAt();
        this.email = applicant.getUser().getEmail();
        this.profileImg = applicant.getUser().getUserInfo().getProfileImageUrl();
        this.position = applicant.getUser().getUserInfo().getPosition();
        this.week = applicant.getWeek();
    }

    public MenteeListResponseDto(Team team) {
        MentoringApplicant mentoringApplicant = team.getMentoringApplicant();
        User user = team.getUser();
        this.nickname = user.getUserInfo().getNickname();
        this.comment = user.getUserInfo().getIntro();
        this.applicationDate = team.getCreatedAt();
        this.email = user.getEmail();
        this.profileImg = user.getUserInfo().getProfileImageUrl();
        this.position = user.getUserInfo().getPosition();
        this.startTime = mentoringApplicant.getStartTime();
        this.endTime = mentoringApplicant.getEndTime();
        this.week = mentoringApplicant.getWeek();
    }
}
