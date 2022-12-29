package com.study.totee.api.controller;

import com.study.totee.api.dto.mentoring.MentoringRequestDto;
import com.study.totee.api.dto.mentoring.MentoringResponseDto;
import com.study.totee.api.service.MentoringService;
import com.study.totee.common.ApiResponse;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.NoAuthException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MentoringController {

    private final MentoringService mentoringService;

    @ApiOperation(value = "멘토링 글 등록" , notes = "멘토링 게시글을 등록합니다 (폼데이터 형식 필수)")
    @PostMapping("/api/v1/mentoring")
    public ApiResponse<Object> createPost(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, @Valid @RequestBody MentoringRequestDto requestDto) throws IOException {
        // 로그인 정보가 없으면 예외 발생
        String id = Optional.ofNullable(principal).orElseThrow(() -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();

        mentoringService.save(id, requestDto);

        return ApiResponse.success("message" , "멘토링 게시글이 성공적으로 등록되었습니다.");
    }

    @ApiOperation(value = "멘토링 게시글 상세보기",
            notes = "mentoringId로 상세보기\n" +
                    "api 주소에 PathVariable 주면 됩니다.")
    @GetMapping("/api/v1/mentoring/{mentoringId}")
    public ApiResponse<Object> getPost(@PathVariable Long mentoringId){

        MentoringResponseDto mentoringResponseDto = mentoringService.findByMentoringId(mentoringId);
        return ApiResponse.success("data", mentoringResponseDto);
    }
}
