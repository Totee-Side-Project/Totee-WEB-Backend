package com.study.totee.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RoleType {
    user("ROLE_USER", "일반 사용자 권한"),
    admin("ROLE_ADMIN", "관리자 권한"),
    totee("ROLE_MENTOR", "멘토 권한");

    private final String code;
    private final String displayName;

    public static RoleType of(String code) {
        return Arrays.stream(RoleType.values())
                .filter(r -> r.getCode().equals(code))
                .findAny()
                .orElse(user);
    }
}