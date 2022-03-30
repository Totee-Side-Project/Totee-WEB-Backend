package com.study.totee.controller;

import com.study.totee.dto.ApiResponse;
import com.study.totee.dto.UserDTO;
import com.study.totee.model.UserEntity;
import com.study.totee.security.TokenProvider;
import com.study.totee.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ApiResponse registerUser(@RequestBody UserDTO userDTO){
        // 요청을 이용해 저장할 사용자 만들기
        UserEntity userEntity = UserEntity.builder()
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();
        // 서비스를 이용해 리포지토리에 사용자 저장
        UserEntity registeredUser = userService.create(userEntity);
        UserDTO responseUserDTO = UserDTO.builder()
                .email(registeredUser.getEmail())
                .username(registeredUser.getUsername())
                .password(registeredUser.getPassword())
                .build();

        return ApiResponse.success("data" , responseUserDTO);
    }

    @PostMapping("/signin")
    public ApiResponse authenticate(@RequestBody UserDTO userDTO) {
        UserEntity user = userService.getByCredentials(
                userDTO.getEmail(),
                userDTO.getPassword(),
                passwordEncoder);

        if(user != null) {
            // 토큰 생성
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .email(user.getEmail())
                    .id(user.getId())
                    .username(user.getUsername())
                    .token(token)
                    .build();
            return ApiResponse.success("token", responseUserDTO);
        } else {
            return ApiResponse.fail("message","Login failed");
        }
    }
}
