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
public class SignUpDTO {
    @ApiModelProperty(example = "기입 X")
    private String token;

    private String id;
    private String email;
    private String username;
    private String password;
    private String nickname;
    private String grade;
    private String major;
}
