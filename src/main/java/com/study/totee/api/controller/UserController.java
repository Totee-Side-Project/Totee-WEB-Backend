package com.study.totee.api.controller;

import com.study.totee.api.dto.user.NicknameRequestDto;
import com.study.totee.api.dto.user.UserInfoRequestDto;
import com.study.totee.api.dto.user.UserInfoResponseDto;
import com.study.totee.api.model.UserEntity;
import com.study.totee.common.ApiResponse;
import com.study.totee.api.model.UserInfoEntity;
import com.study.totee.api.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "로그인한 유저 정보" , notes = "유저 관련 정보를 확인합니다.")
    @GetMapping("/api/v1/info")
    public ApiResponse getUserInfo(@AuthenticationPrincipal User principal) {
        UserEntity user = userService.getUser(principal.getUsername());
        UserInfoEntity userInfoEntity = user.getUserInfo();

        UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.builder()
                .grade(userInfoEntity.getGrade())
                .major(userInfoEntity.getMajor())
                .nickname(userInfoEntity.getNickname())
                .roleType(user.getRoleType())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .build();

        return ApiResponse.success("data" , userInfoResponseDto);
    }

    @ApiOperation(value = "유저 정보 저장하기", notes = "신규유저의 유저 관련 정보를 저장합니다.")
    @PostMapping("/api/v1/info")
    public ApiResponse createUserInfo(@AuthenticationPrincipal User principal, @RequestBody UserInfoRequestDto userInfoRequestDto){

        userService.createUserInfo(principal.getUsername(), userInfoRequestDto);
        return ApiResponse.success("data", "SUCCESS");
    }

    @ApiOperation(value = "닉네임 중복검사" , notes = "닉네임이 중복이면 false 중복이아니면 true 을 반환합니다")
    @PostMapping("/api/v1/validation/nickname")
    public ApiResponse validateNickname(@RequestBody NicknameRequestDto nicknameRequestDto){
        // 있으면
        return ApiResponse.success("data" ,!userService.validate(nicknameRequestDto.getNickname()));
    }
}