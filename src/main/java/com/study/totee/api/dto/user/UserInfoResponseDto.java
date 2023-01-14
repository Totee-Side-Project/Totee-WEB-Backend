package com.study.totee.api.dto.user;

import com.study.totee.api.model.User;
import com.study.totee.api.model.UserInfo;
import com.study.totee.type.PositionType;
import com.study.totee.type.RoleType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Getter
@NoArgsConstructor
public class UserInfoResponseDto {
    @ApiModelProperty(example = "이메일")
    private String email;
    @ApiModelProperty(example = "닉네임")
    private String nickname;
    @ApiModelProperty(example = "포지션 ex) FRONT_END, BACK_END, DESIGN, GAME, ML, PRODUCT_MANAGER, iOS, ANDROID, OTHERS")
    private PositionType position;
    @ApiModelProperty(example = "유저 프로필 이미지 URL")
    private String profileImageUrl;
    @ApiModelProperty(example = "유저 등급")
    private RoleType roleType;
    @ApiModelProperty(example = "자기소개")
    private String intro;
    @ApiModelProperty(example = "배경 이미지")
    private String backgroundImageUrl;
    @ApiModelProperty(example = "1")
    private int StudyNum;
    @ApiModelProperty(example = "1")
    private int MentoringNum;

    public UserInfoResponseDto(User user){
        this.email = user.getEmail();
        this.nickname = user.getUserInfo().getNickname();
        this.position = user.getUserInfo().getPosition();
        this.roleType = user.getRoleType();
        this.intro = user.getUserInfo().getIntro();
        this.profileImageUrl = user.getUserInfo().getProfileImageUrl();
        this.backgroundImageUrl = user.getUserInfo().getBackgroundImageUrl();
        this.StudyNum = user.getUserInfo().getStudyNum();
        this.MentoringNum = user.getUserInfo().getMentoringNum();
    }

    public UserInfoResponseDto(UserInfo userInfo){
        this.nickname = userInfo.getNickname();
        this.position = userInfo.getPosition();
        this.intro = userInfo.getIntro();
        this.profileImageUrl = userInfo.getProfileImageUrl();
        this.backgroundImageUrl = userInfo.getBackgroundImageUrl();
//        this.roleType = userInfo.getUser().getRoleType();
//        this.email = userInfo.getUser().getEmail();
    }

    public UserInfoResponseDto(UserInfo userInfo, User user){
        this.nickname = userInfo.getNickname();
        this.position = userInfo.getPosition();
        this.intro = userInfo.getIntro();
        this.profileImageUrl = userInfo.getProfileImageUrl();
        this.backgroundImageUrl = userInfo.getBackgroundImageUrl();
        this.roleType = user.getRoleType();
        this.email = user.getEmail();
    }
}
