package com.study.totee.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Marine
 * @date : 2022-08-02
 * @description : 로컬테스트 전용 Dto입니다.
 */

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
