package com.study.totee.api.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;


@Data
@Getter
@NoArgsConstructor
public class NicknameRequestDto {
    @ApiModelProperty(example = "닉네임")
    private String nickname;
}
