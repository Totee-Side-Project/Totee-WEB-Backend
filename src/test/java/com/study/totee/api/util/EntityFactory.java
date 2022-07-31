package com.study.totee.api.util;

import com.study.totee.api.model.*;
import com.study.totee.type.PeriodType;
import com.study.totee.type.PositionType;
import com.study.totee.type.ProviderType;
import com.study.totee.type.RoleType;

import java.time.LocalDateTime;

public class EntityFactory {

    private static LocalDateTime today = LocalDateTime.now();

    public static CategoryEntity category(){
        return CategoryEntity.builder()
                .categoryName("TEST")
                .build();
    }

    public static PostEntity post(){

        return PostEntity.builder()
                .title("TEST")
                .content("TEST")
                .user(user())
                .period(PeriodType.ShortTerm)
                .onlineOrOffline("ONLINE")
                .status("Y")
                .commentNum(0)
                .view(0)
                .likeNum(0)
                .recruitNum("1~2명")
                .contactMethod("노션")
                .contactLink("https://www.naver.com")
                .createdAt(today)
                .modifiedAt(today)
                .build();
    }

    public static UserInfoEntity userInfo(){
        return UserInfoEntity.builder()
                .nickname("TEST")
                .positionList(null)
                .position(PositionType.BACK_END)
                .createdAt(today)
                .modifiedAt(today)
                .build();

    }

    public static UserEntity user(){

        UserInfoEntity userInfoEntity = userInfo();

        return UserEntity.builder()
                .id("TEST")
                .username("TEST")
                .email("NO_MAIL")
                .emailVerifiedYn("Y")
                .profileImageUrl("NO_IMAGE")
                .providerType(ProviderType.LOCAL)
                .password("NO_PASSWORD")
                .roleType(RoleType.USER)
                .userInfo(userInfoEntity)
                .createdAt(today)
                .modifiedAt(today)
                .build();
    }

    public static CommentEntity comment(){
        return CommentEntity.builder()
                .content("TEST")
                .profileImageUrl("TEST")
                .build();
    }

    public static LikeEntity like(){
        return LikeEntity.builder()
                .build();
    }

    public static ReplyEntity reply(){
        return ReplyEntity.builder()
                .content("TEST")
                .nickname("TEST")
                .profileImageUrl("TEST")
                .build();
    }

}

