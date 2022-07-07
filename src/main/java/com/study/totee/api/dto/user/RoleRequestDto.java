package com.study.totee.api.dto.user;

import com.study.totee.type.RoleType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequestDto {
    @ApiModelProperty(example = "닉네임")
    private String nickname;
    @ApiModelProperty(example = "유저 등급")
    private RoleType roleType;
}
