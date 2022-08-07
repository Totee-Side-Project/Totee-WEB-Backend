package com.study.totee.api.dto.user;

import com.study.totee.type.PositionType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;


@Data
@Getter
@NoArgsConstructor
public class UserInfoRequestDto {
    @ApiModelProperty(example = "닉네임")
    private String nickname;

    @ApiModelProperty(example = "포지션 ex) FRONT_END, BACK_END, DESIGN, GAME, ML, PRODUCT_MANAGER, iOS, ANDROID, OTHERS")
    private PositionType position;

    @ApiModelProperty(example = "유저 프로필 이미지")
    private MultipartFile profileImage;
}
