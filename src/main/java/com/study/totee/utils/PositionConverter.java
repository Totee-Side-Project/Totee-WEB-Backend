package com.study.totee.utils;

import com.study.totee.api.model.PositionEntity;
import com.study.totee.api.model.PostEntity;
import com.study.totee.api.model.UserInfoEntity;
import com.study.totee.type.PositionType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
public class PositionConverter {

    public List<PositionEntity> convertStringToPositionEntity(List<String> position, UserInfoEntity userInfo, PostEntity post){
        List<PositionEntity> positionList = new ArrayList<>();
        for (String pos : position) {
            switch (pos) {
                case "프론트엔드":
                    if (userInfo == null) {
                        positionList.add(new PositionEntity(PositionType.FRONT_END, post));
                    } else {
                        positionList.add(new PositionEntity(PositionType.FRONT_END, userInfo));
                    }
                    break;
                case "백엔드":
                    if (userInfo == null) {
                        positionList.add(new PositionEntity(PositionType.BACK_END, post));
                    } else {
                        positionList.add(new PositionEntity(PositionType.BACK_END, userInfo));
                    }
                    break;
                case "디자인":
                    if (userInfo == null) {
                        positionList.add(new PositionEntity(PositionType.DESIGN, post));
                    } else {
                        positionList.add(new PositionEntity(PositionType.DESIGN, userInfo));
                    }
                    break;
                case "게임":
                    if (userInfo == null) {
                        positionList.add(new PositionEntity(PositionType.GAME, post));
                    } else {
                        positionList.add(new PositionEntity(PositionType.GAME, userInfo));
                    }
                    break;
                case "ML":
                    if (userInfo == null) {
                        positionList.add(new PositionEntity(PositionType.MACHINE_LEARNING, post));
                    } else {
                        positionList.add(new PositionEntity(PositionType.MACHINE_LEARNING, userInfo));
                    }
                    break;
                case "PM":
                    if (userInfo == null) {
                        positionList.add(new PositionEntity(PositionType.PRODUCT_MANAGER, post));
                    } else {
                        positionList.add(new PositionEntity(PositionType.PRODUCT_MANAGER, userInfo));
                    }
                    break;
                case "iOS":
                    if (userInfo == null) {
                        positionList.add(new PositionEntity(PositionType.iOS, post));
                    } else {
                        positionList.add(new PositionEntity(PositionType.iOS, userInfo));
                    }
                    break;
                case "안드로이드":
                    if (userInfo == null) {
                        positionList.add(new PositionEntity(PositionType.ANDROID, post));
                    } else {
                        positionList.add(new PositionEntity(PositionType.ANDROID, userInfo));
                    }
                    break;
                case "기타":
                    if (userInfo == null) {
                        positionList.add(new PositionEntity(PositionType.OTHERS, post));
                    } else {
                        positionList.add(new PositionEntity(PositionType.OTHERS, userInfo));
                    }
                    break;
            }
        }
        return positionList;
    }

    public  List<PositionType> convertStringToPosition(List<String> position){
        List<PositionType> positionTypeList = new ArrayList<>();
        for (String pos : position) {
            switch (pos) {
                case "프론트엔드":
                    positionTypeList.add(PositionType.FRONT_END);
                    break;
                case "백엔드":
                    positionTypeList.add(PositionType.BACK_END);
                    break;
                case "디자인":
                    positionTypeList.add(PositionType.DESIGN);
                    break;
                case "게임":
                    positionTypeList.add(PositionType.GAME);
                    break;
                case "ML":
                    positionTypeList.add(PositionType.MACHINE_LEARNING);
                    break;
                case "PM":
                    positionTypeList.add(PositionType.PRODUCT_MANAGER);
                    break;
                case "iOS":
                    positionTypeList.add(PositionType.iOS);
                    break;
                case "안드로이드":
                    positionTypeList.add(PositionType.ANDROID);
                    break;
                case "기타":
                    positionTypeList.add(PositionType.OTHERS);
                    break;
            }
        }
        if(positionTypeList.size() == 0){
            Collections.addAll(positionTypeList, PositionType.values());
        }
        return positionTypeList;
    }

    public List<String> convertPositionEntityToString(Set<PositionEntity> position){
        List<String> positionList = new ArrayList<>();
        for (PositionEntity pos : position) {
            positionList.add(pos.getPosition().getPosition());
        }
        return positionList;
    }
}
