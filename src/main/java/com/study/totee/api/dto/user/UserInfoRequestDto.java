package com.study.totee.api.dto.user;

import com.study.totee.type.PositionType;
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
    @ApiModelProperty(example = "닉네임")
    private String nickname;
    @ApiModelProperty(example = "포지션 ex) FRONT_END, BACK_END, DESIGN, GAME, ML, PRODUCT_MANAGER, iOS, ANDROID, OTHERS")
    private PositionType position;
    @ApiModelProperty(example = "유저 프로필 이미지")
    private MultipartFile profileImage;
}
