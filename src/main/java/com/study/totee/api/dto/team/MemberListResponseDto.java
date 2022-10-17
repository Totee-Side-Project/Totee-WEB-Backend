package com.study.totee.api.dto.team;

import com.study.totee.api.model.Applicant;
import com.study.totee.api.model.Team;
import com.study.totee.api.model.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Getter
@NoArgsConstructor
public class MemberListResponseDto {

    @ApiModelProperty(example = "지원하기")
    private String nickname;

    @ApiModelProperty(example = "지원메세지")
    private String message;

    @ApiModelProperty(example = "지원 날짜")
    private LocalDateTime applicationDate;

    public MemberListResponseDto(Team team) {
        User user = team.getUser();
        this.nickname = user.getUserInfo().getNickname();
        this.applicationDate = team.getCreatedAt();
    }

    public MemberListResponseDto(Applicant applicant) {
        User user = applicant.getUser();
        this.nickname = user.getUserInfo().getNickname();
        this.applicationDate = applicant.getCreatedAt();
    }
}
