package com.study.totee.type;

import lombok.Getter;

@Getter
public enum PositionType {
    FRONT_END("프론트엔드"),
    BACK_END("백엔드"),
    DESIGN("디자인"),
    GAME("게임"),
    ML("머신러닝"),
    PM("PM"),
    iOS("iOS"),
    ANDROID("안드로이드"),
    OTHERS("기타");

    private final String position;

    PositionType(String position) {
        this.position = position;
    }
}
