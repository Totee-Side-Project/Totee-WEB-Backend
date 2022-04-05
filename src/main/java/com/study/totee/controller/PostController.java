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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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

    @ApiOperation(value = "post 글 목록 불러오기",
            notes = "글 목록 불러오기 ex : api/v1/post/list?page=0&size=5&sort=postId.desc\n" +
                    "page : 몇번째 page 불러올건지\n" +
                    "size : 1페이지 당 개수\n" +
                    "sort : 어떤것을 기준으로 정렬 할 것 인지\n" +
                    "default : Size  16 , sort postId\n" +
                    "임시저장글은 불러오지 않음\n")
    @GetMapping("/api/v1/post/list")
    public ApiResponse findPostAll( @PageableDefault(size = 16 ,sort = "postId",direction = Sort.Direction.DESC ) Pageable pageable){
        Page post = postService.findPostAll(pageable);
        return ApiResponse.success("data", post);
    }
}
