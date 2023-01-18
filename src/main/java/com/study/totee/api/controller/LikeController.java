package com.study.totee.api.controller;

import com.study.totee.common.ApiResponse;
import com.study.totee.api.service.LikeService;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.NoAuthException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @ApiOperation(value = "스터디 좋아요!", notes = "이미 좋아요 되어있으면 좋아요는 취소됩니다.")
    @PostMapping("/api/v1/post/like/{postId}")
    public ApiResponse<Object> like(@AuthenticationPrincipal User principal , @PathVariable Long postId){
        // 로그인이 되어 있지 않으면 예외를 던진다.
        String id = Optional.ofNullable(principal).orElseThrow(()->
                new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();
        likeService.like(id, postId);

        return ApiResponse.success("message","게시글에 좋아요를 눌렀습니다.");
    }

    @ApiOperation(value = "스터디 좋아요 여부", notes = "해당 포스트에 내가 '좋아요'를 눌렀는 지 확인합니다. 로그인이 안된 유저는 무조건 false를 반환합니다.")
    @GetMapping("/api/v1/post/isLike/{postId}")
    public ApiResponse<Object> isLike(@AuthenticationPrincipal User principal, @PathVariable Long postId){
        if (principal == null) {
            return ApiResponse.success("data", "");
        }
        boolean isLike = likeService.isLike(principal.getUsername(), postId);
        return ApiResponse.success("data", isLike);
    }

    @ApiOperation(value = "멘토링 좋아요!", notes = "이미 좋아요 되어있으면 좋아요는 취소됩니다.")
    @PostMapping("/api/v1/mentoring/like/{mentoringId}")
    public ApiResponse<Object> mentoringLike(@AuthenticationPrincipal User principal , @PathVariable Long mentoringId){
        // 로그인이 되어 있지 않으면 예외를 던진다.
        String id = Optional.ofNullable(principal).orElseThrow(()->
                new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();
        likeService.mentoringLike(id, mentoringId);

        return ApiResponse.success("message","게시글에 좋아요를 눌렀습니다.");
    }

    @ApiOperation(value = "멘토링 좋아요 여부", notes = "해당 포스트에 내가 '좋아요'를 눌렀는 지 확인합니다. 로그인이 안된 유저는 무조건 false를 반환합니다.")
    @GetMapping("/api/v1/mentoring/isLike/{mentoringId}")
    public ApiResponse<Object> isMenotirngLike(@AuthenticationPrincipal User principal, @PathVariable Long mentoringId){
        if (principal == null) {
            return ApiResponse.success("data", "");
        }
        boolean isLike = likeService.isMentoringLike(principal.getUsername(), mentoringId);
        return ApiResponse.success("data", isLike);
    }
}