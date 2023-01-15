package com.study.totee.api.controller;

import com.study.totee.api.dto.applicant.ApplicantRequestDto;
import com.study.totee.api.dto.mentoringApplicant.MentoringApplicantRequestDto;
import com.study.totee.api.dto.team.MemberListResponseDto;
import com.study.totee.api.dto.team.MenteeListResponseDto;
import com.study.totee.api.model.Mentoring;
import com.study.totee.api.model.Post;
import com.study.totee.api.service.MentoringApplicantService;
import com.study.totee.api.service.MentoringService;
import com.study.totee.common.ApiResponse;
import com.study.totee.exption.ErrorCode;
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
public class MentoringApplicantController {

    private final MentoringApplicantService mentoringApplicantService;
    private final MentoringService mentoringService;

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

    @ApiOperation(value = "멘토링 지원목록", notes = "멘티로 지원한 사람들 목록 볼 수 있음")
    @GetMapping("/api/v2/applicant/{mentoringId}")
    public ApiResponse<Object> getMentoringApplicant(@PathVariable Long mentoringId) {

        Mentoring mentoring = mentoringService.findByMentoringId(mentoringId);
        List<MenteeListResponseDto> responseDto = mentoringApplicantService.getMentoringApplicant(mentoring);
        return ApiResponse.success("data" , responseDto);

    }
}
