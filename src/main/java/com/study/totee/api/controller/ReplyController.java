package com.study.totee.api.controller;

import com.study.totee.api.dto.reply.ReplyRequestDto;
import com.study.totee.api.service.ReplyService;
import com.study.totee.common.ApiResponse;
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
public class ReplyController {
    private final ReplyService replyService;

    @ApiOperation(value = "대댓글 등록", notes = "대댓글을 등록합니다")
    @PostMapping("/api/v1/reply")
    public ApiResponse saveReply(@AuthenticationPrincipal User principal, @RequestBody ReplyRequestDto replyRequestDto) {
        // 로그인이 되어 있지 않으면 예외를 던진다.
        String id = Optional.ofNullable(principal).orElseThrow(
                () -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();
        replyService.save(replyRequestDto, id);
        return ApiResponse.success("message" , "대댓글을 성공적으로 등록했습니다.");
    }

    @ApiOperation(value = "대댓글 수정", notes = "대댓글을 수정합니다")
    @PutMapping("/api/v1/reply/{replyId}")
    public ApiResponse updateReply(@AuthenticationPrincipal User principal, @PathVariable Long replyId, @RequestBody ReplyRequestDto replyRequestDto){
        // 로그인이 되어 있지 않으면 예외를 던진다.
        String id = Optional.ofNullable(principal).orElseThrow(
                () -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();
        replyService.update(replyRequestDto, id, replyId);
        return ApiResponse.success("message", "대댓글을 성공적으로 수정했습니다.");
    }

    @ApiOperation(value = "대댓글 삭제", notes = "대댓글을 삭제합니다")
    @DeleteMapping("/api/v1/reply/{replyId}")
    public ApiResponse deleteReply(@AuthenticationPrincipal User principal, @PathVariable Long replyId){
        // 로그인이 되어 있지 않으면 예외를 던진다.
        String id = Optional.ofNullable(principal).orElseThrow(
                () -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();
        replyService.delete(replyId, id);
        return ApiResponse.success("message", "대댓글을 성공적으로 삭제했습니다.");
    }
}
