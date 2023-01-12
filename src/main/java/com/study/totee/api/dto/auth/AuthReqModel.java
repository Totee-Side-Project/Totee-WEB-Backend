package com.study.totee.api.dto.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthReqModel {
    @ApiModelProperty(example = "123")
    private String id;
    @ApiModelProperty(example = "123")
    private String password;
}