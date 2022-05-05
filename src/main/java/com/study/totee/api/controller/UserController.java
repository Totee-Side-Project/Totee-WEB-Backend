package com.study.totee.api.controller;

import com.study.totee.common.ApiResponse;
import com.study.totee.api.dto.UserInfoDTO;
import com.study.totee.api.model.UserInfoEntity;
import com.study.totee.api.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "로그인한 유저 정보" , notes = "유저 이름과 함께 정보를 확인 가능")
    @GetMapping("/api/v1/info")
    public ApiResponse getUserInfo(@AuthenticationPrincipal String id) {
        UserInfoEntity userInfoEntity = userService.getUser(id).getUserInfo();
        UserInfoDTO userInfoDTO = UserInfoDTO.builder()
                .gender(userInfoEntity.getGender())
                .major(userInfoEntity.getMajor())
                .phone(userInfoEntity.getPhone())
                .studentId(userInfoEntity.getStudentId())
                .username(userInfoEntity.getUser().getUsername())
                .build();
        return ApiResponse.success("data" , userInfoDTO);
    }
}