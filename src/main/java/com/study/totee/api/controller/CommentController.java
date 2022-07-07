package com.study.totee.api.controller;

import com.study.totee.api.dto.comment.CommentRequestDto;
import com.study.totee.common.ApiResponse;
import com.study.totee.api.service.CommentService;
import com.study.totee.api.service.UserService;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.NoAuthException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @ApiOperation(value = "댓글 등록", notes = "댓글을 등록합니다")
    @PostMapping("/api/v1/comment")
    public ApiResponse saveComment(@AuthenticationPrincipal User principal, @RequestBody CommentRequestDto commentRequestDto) {
        // 로그인이 되어 있지 않으면 예외를 던진다.
        String id = Optional.ofNullable(principal).orElseThrow(
                () -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();
        commentService.save(commentRequestDto, id);
        return ApiResponse.success("message" , "댓글을 성공적으로 등록했습니다.");
    }

    @ApiOperation(value = "댓글 수정", notes = "댓글을 수정합니다")
    @PutMapping("/api/v1/comment/{commentId}")
    public ApiResponse updateComment(@AuthenticationPrincipal User principal, @PathVariable Long commentId, CommentRequestDto commentRequestDto){
        // 로그인이 되어 있지 않으면 예외를 던진다.
        String id = Optional.ofNullable(principal).orElseThrow(
                () -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();
        commentService.update(commentRequestDto, id, commentId);
        return ApiResponse.success("message", "댓글을 성공적으로 수정했습니다.");
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글을 삭제합니다")
    @DeleteMapping("/api/v1/comment/{commentId}")
    public ApiResponse deleteComment(@AuthenticationPrincipal User principal, @PathVariable Long commentId){
        // 로그인이 되어 있지 않으면 예외를 던진다.
        String id = Optional.ofNullable(principal).orElseThrow(
                () -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();
        commentService.delete(commentId, principal.getUsername());
        return ApiResponse.success("message", "댓글을 성공적으로 삭제했습니다.");
    }
}