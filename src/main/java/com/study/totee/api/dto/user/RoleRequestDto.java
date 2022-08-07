package com.study.totee.api.dto.user;

import com.study.totee.type.RoleType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;


@Data
@Getter
@NoArgsConstructor
public class RoleRequestDto {
    @ApiModelProperty(example = "닉네임")
    private String nickname;
    @ApiModelProperty(example = "유저 등급")
    private RoleType roleType;
}
