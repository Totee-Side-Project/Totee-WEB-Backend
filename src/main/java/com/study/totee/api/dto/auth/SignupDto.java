package com.study.totee.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {
    private String token;

    private String id;
    private String email;
    private String username;
    private String password;
    private String nickname;
    private String grade;
    private String major;
}
