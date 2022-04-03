package com.study.totee.controller;

import com.study.totee.dto.ApiResponse;
import com.study.totee.dto.PostDTO;
import com.study.totee.model.PostEntity;
import com.study.totee.model.UserEntity;
import com.study.totee.service.PostService;
import com.study.totee.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @ApiOperation(value = "포스트 등록" , notes = "포스트를 등록합니다")
    @PostMapping("/api/v1/post")
    public ApiResponse getUserInfo(@AuthenticationPrincipal String id, @RequestBody PostDTO postDTO){
        try {
            Optional<UserEntity> user = userService.getUserId(id);
            PostEntity postEntity = PostEntity.builder()
                    .content(postDTO.getTitle())
                    .intro(postDTO.getIntro())
                    .title(postDTO.getTitle())
                    .status(false)
                    .user(user.get())
                    .build();

            postService.save(postEntity);
            return ApiResponse.success("message" , "SUCCESS");
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.fail("message", "Failed");
        }
    }
}
