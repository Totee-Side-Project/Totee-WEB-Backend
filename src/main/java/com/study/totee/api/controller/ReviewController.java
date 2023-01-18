package com.study.totee.api.controller;

import com.study.totee.api.dto.reply.ReplyRequestDto;
import com.study.totee.api.dto.review.ReviewRequestDto;
import com.study.totee.api.dto.review.ReviewResponseDto;
import com.study.totee.api.service.ReviewService;
import com.study.totee.common.ApiResponse;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.NoAuthException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @ApiOperation(value = "리뷰 등록", notes = "등록되면 수정 삭제 불가, 해당 멘토링의 멤버일 경우 등록 가능")
    @PostMapping("/api/v1/review/{mentoringId}")
    public ApiResponse<Object> saveReview(@AuthenticationPrincipal User principal, @Valid @RequestBody ReviewRequestDto dto,
                                          @PathVariable Long mentoringId) {
        // 로그인이 되어 있지 않으면 예외를 던진다.
        String id = Optional.ofNullable(principal).orElseThrow(
                () -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();
        reviewService.save(id, dto, mentoringId);
        return ApiResponse.success("message" , "리뷰를 성공적으로 등록했습니다.");
    }

    @ApiOperation(value = "해당 멘토링의 리뷰 보기")
    @GetMapping("/api/v1/review/{mentoringId}")
    public ApiResponse<Object> findReview(@PathVariable Long mentoringId,
                                          @PageableDefault(size = 20 , sort = "id",direction = Sort.Direction.DESC)Pageable pageable) {
        Page<ReviewResponseDto> page = reviewService.findAll(mentoringId, pageable);
        return ApiResponse.success("data", page);
    }
}
