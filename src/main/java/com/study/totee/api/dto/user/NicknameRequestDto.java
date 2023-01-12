package com.study.totee.api.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NicknameRequestDto {
    @ApiModelProperty(example = "닉네임")
    @NotBlank
    private String nickname;
}
