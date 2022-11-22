package com.study.totee.api.controller;

import com.study.totee.api.dto.applicant.ApplicantRequestDto;
import com.study.totee.api.dto.team.MemberListResponseDto;
import com.study.totee.api.model.Post;
import com.study.totee.api.model.User;
import com.study.totee.api.service.ApplicantService;
import com.study.totee.api.service.PostService;
import com.study.totee.common.ApiResponse;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.ForbiddenException;
import com.study.totee.exption.NoAuthException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ApplicantController {
    private final ApplicantService applicantService;
    private final PostService postService;

    @ApiOperation(value = "스터디 지원하기", notes = "스터디 지원하기")
    @PostMapping("/api/v1/applicant/{postId}")
    public ApiResponse<Object> apply(@PathVariable Long postId,
                                     @RequestBody ApplicantRequestDto requestDto,
                                     @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        String userId = Optional.ofNullable(principal.getUsername()).orElseThrow(
                () -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)
        );

        applicantService.applyPost(userId, postId, requestDto.getMessage());
        return ApiResponse.success("message" , "성공적으로 지원되었습니다.");
    }

    @ApiOperation(value = "스터디 지원취소")
    @DeleteMapping("/api/v1/applicant/{postId}")
    public ApiResponse<Object> cancelApply(@PathVariable Long postId,
                                     @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        String userId = Optional.ofNullable(principal.getUsername()).orElseThrow(
                () -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)
        );
        applicantService.cancelApply(userId, postId);

        return ApiResponse.success("message" , "지원이 취소되었습니다.");

    }

    @ApiOperation(value = "스터디 지원현황")
    @GetMapping("/api/v1/applicant/{postId}")
    public ApiResponse<Object> getApplicant(@PathVariable Long postId,
                                               @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {

        Post post = postService.findByPostId(postId);
        List<MemberListResponseDto> responseDto = applicantService.getApplicant(post);
        return ApiResponse.success("data" , responseDto);

    }
}
