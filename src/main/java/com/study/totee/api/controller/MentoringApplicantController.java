package com.study.totee.api.controller;

import com.study.totee.api.dto.applicant.ApplicantRequestDto;
import com.study.totee.api.dto.mentoringApplicant.MentoringApplicantRequestDto;
import com.study.totee.api.service.MentoringApplicantService;
import com.study.totee.common.ApiResponse;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.NoAuthException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MentoringApplicantController {

    private final MentoringApplicantService mentoringApplicantService;

    @ApiOperation(value = "멘토링 지원하기", notes = "해당 멘토링에 멘티로 지원하는 기능")
    @PostMapping("/api/v2/applicant/{mentoringId}")
    public ApiResponse<Object> apply(@PathVariable Long mentoringId,
                                     @RequestBody MentoringApplicantRequestDto requestDto,
                                     @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        String userId = Optional.ofNullable(principal.getUsername()).orElseThrow(
                () -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)
        );

        mentoringApplicantService.applyMentoring(userId, mentoringId, requestDto);
        return ApiResponse.success("message" , "성공적으로 지원되었습니다.");
    }
}
