package com.study.totee.api.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NicknameRequestDto {
    @ApiModelProperty(example = "닉네임")
    private String nickname;
}
