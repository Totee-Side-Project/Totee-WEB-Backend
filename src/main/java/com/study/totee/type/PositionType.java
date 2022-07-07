package com.study.totee.type;

import lombok.Getter;

@Getter
public enum PositionType {
    FRONT_END("FrontEnd"),
    BACK_END("BackEnd"),
    DESIGN("Design"),
    GAME("Game"),
    MACHINE_LEARNING("MachineLearning"),
    PRODUCT_MANAGER("ProductManager"),
    iOS("iOS"),
    ANDROID("Android"),
    OTHERS("Others");

    private final String position;

    PositionType(String position) {
        this.position = position;
    }
}
