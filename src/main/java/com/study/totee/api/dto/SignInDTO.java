package com.study.totee.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInDTO {
    private String email;
    private String password;
    @ApiModelProperty(example = "기입 X")
    private String username;
    @ApiModelProperty(example = "기입 X")
    private String token;
}
