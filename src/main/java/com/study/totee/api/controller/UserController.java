package com.study.totee.api.controller;

import com.study.totee.api.dto.user.NicknameRequestDto;
import com.study.totee.api.dto.user.UserInfoRequestDto;
import com.study.totee.api.dto.user.UserInfoResponseDto;
import com.study.totee.api.model.UserEntity;
import com.study.totee.common.ApiResponse;
import com.study.totee.api.model.UserInfoEntity;
import com.study.totee.api.service.UserService;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.ForbiddenException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "로그인한 유저 정보" , notes = "유저 관련 정보를 확인합니다.")
    @GetMapping("/api/v1/info")
    public ApiResponse getUserInfo(@AuthenticationPrincipal User principal) {
        if(principal == null) {
            throw new ForbiddenException(ErrorCode.NO_USER_ERROR);
        }
        UserEntity user = userService.getUser(principal.getUsername());
        UserInfoEntity userInfoEntity = user.getUserInfo();

        UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.builder()
                .position(userInfoEntity.getPosition())
                .nickname(userInfoEntity.getNickname())
                .roleType(user.getRoleType())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .build();

        return ApiResponse.success("data" , userInfoResponseDto);
    }

    @ApiOperation(value = "유저 정보 저장하기", notes = "신규유저의 유저 관련 정보를 저장합니다.")
    @PostMapping("/api/v1/info")
    public ApiResponse createUserInfo(@AuthenticationPrincipal User principal, @ModelAttribute @Valid @RequestBody UserInfoRequestDto userInfoRequestDto) throws IOException {

        userService.createUserInfo(principal.getUsername(), userInfoRequestDto);
        return ApiResponse.success("data", "SUCCESS");
    }

    @ApiOperation(value = "닉네임 중복 확인" , notes = "사용 가능한 닉네임이면 true 을 반환하고 사용 불가능한 닉네임이면 예외 처리" +
            "닉네임 2자 이상, 5자 이하 길이만 가능합니다.")
    @PostMapping("/api/v1/validate/nickname")
    public ApiResponse isNicknameDuplicate(@RequestParam NicknameRequestDto nicknameRequestDto){
        if(nicknameRequestDto.getNickname().length() < 2 || nicknameRequestDto.getNickname().length() > 5){
            throw new BadRequestException(ErrorCode.INVALID_INPUT_ERROR);
        }
        if (userService.isNicknameDuplicate(nicknameRequestDto.getNickname())) {
            throw new BadRequestException(ErrorCode.ALREADY_EXIST_NICKNAME_ERROR);
        }
        return ApiResponse.success("data" ,true);
    }
}