package com.study.totee.controller;

import com.study.totee.dto.ApiResponse;
import com.study.totee.dto.UserDTO;
import com.study.totee.model.UserEntity;
import com.study.totee.model.UserInfoEntity;
import com.study.totee.persistence.UserRefreshTokenRepository;
import com.study.totee.security.TokenProvider;
import com.study.totee.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @ApiOperation(value = "회원가입", notes = "유저의 정보로 회원가입합니다.")
    @PostMapping("/signup")
    public ApiResponse registerUser(@RequestBody UserDTO userDTO){
        // 요청을 이용해 저장할 사용자 만들기
        UserEntity userEntity = UserEntity.builder()
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();

        UserInfoEntity userInfoEntity = UserInfoEntity.builder()
                .gender(userDTO.getGender())
                .phone(userDTO.getPhone())
                .major(userDTO.getMajor())
                .studentId(userDTO.getStudentId())
                .user(userEntity)
                .build();

        userEntity.setUserInfo(userInfoEntity);

        // 서비스를 이용해 리포지토리에 사용자 저장
        userService.create(userEntity, userInfoEntity);

        return ApiResponse.success("message" , "SUCCESS");
    }

    @ApiOperation(value = "로그인", notes = "이메일과 비밀번호로 로그인합니다.")
    @PostMapping("/signin")
    public ApiResponse authenticate(@RequestBody UserDTO userDTO) {
        UserEntity user = userService.getByCredentials(
                userDTO.getEmail(),
                userDTO.getPassword(),
                passwordEncoder);

        if(user != null) {
            // 토큰 생성
            final String token = tokenProvider.create(user);
//            final UserDTO responseUserDTO = UserDTO.builder()
//                    .email(user.getEmail())
//                    .id(user.getId())
//                    .username(user.getUsername())
//                    .token(token)
//                    .build();
            return ApiResponse.success("token", token);
        } else {
            return ApiResponse.fail("message","Login failed");
        }
    }


}
