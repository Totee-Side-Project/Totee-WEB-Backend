package com.study.totee.api.dto.user;

import com.study.totee.api.Type.PositionType;
import com.study.totee.oauth.entity.RoleType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRequestDto {
    @ApiModelProperty(example = "학년")
    private String grade;
    @ApiModelProperty(example = "닉네임")
    private String nickname;
    @ApiModelProperty(example = "포지션 ex) DESIGN, BACKEND, FRONTEND, ML, GAME, IOS, ANDROID, OTHERS")
    private PositionType position;
    @ApiModelProperty(example = "유저 프로필 이미지")
    private MultipartFile profileImage;
}
