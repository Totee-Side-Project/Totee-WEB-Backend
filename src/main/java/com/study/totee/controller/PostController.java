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
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
                    .content(postDTO.getContent())
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

    @ApiOperation(value = "post 상세보기",
            notes = "PostId로 상세보기\n" +
                    "api 주소에 PathVariable 주면 됩니다.")
    @GetMapping("/api/v1/post/{postId}")
    public ApiResponse findByPostId(@PathVariable Long postId, HttpServletRequest request, HttpServletResponse response){
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + postId.toString() + "]")) {
                postService.updateView(postId);
                oldCookie.setValue(oldCookie.getValue() + "_[" + postId + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(oldCookie);
            }
        } else {
            postService.updateView(postId);
            Cookie newCookie = new Cookie("postView","[" + postId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }
        return ApiResponse.success("data",postService.findByPostId(postId));
    }

    @ApiOperation(value = "post 업데이트", notes = "게시글을 수정합니다")
    @PutMapping("/api/v1/post/{postId}")
    public ApiResponse postUpdate(@AuthenticationPrincipal String userId, @RequestBody PostDTO postDTO, @PathVariable Long postId){
        PostEntity post = postService.update(postDTO, postId, userId);
        PostDTO response = PostDTO.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .intro(post.getIntro())
                .build();
        return ApiResponse.success("data", response);
    }

    @ApiOperation(value = "post 삭제" , notes = "게시글을 삭제합니다")
    @DeleteMapping("/api/v1/post/{postId}")
    public ApiResponse deletePost(@AuthenticationPrincipal String userId, @PathVariable Long postId){
        postService.delete(postId, userId);
        return ApiResponse.success("message" , "SUCCESS");
    }
}
