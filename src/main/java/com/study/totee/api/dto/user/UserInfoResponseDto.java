package com.study.totee.api.dto.user;

import com.study.totee.oauth.entity.RoleType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponseDto {
    @ApiModelProperty(example = "학년")
    private String grade;
    @ApiModelProperty(example = "이메일")
    private String email;
    @ApiModelProperty(example = "닉네임")
    private String nickname;
    @ApiModelProperty(example = "직종")
    private String major;
    @ApiModelProperty(example = "유저 프로필 이미지 URL")
    private String profileImageUrl;
    @ApiModelProperty(example = "유저 등급")
    private RoleType roleType;
}
