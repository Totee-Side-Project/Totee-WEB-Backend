package com.study.totee.api.controller;

import com.study.totee.api.dto.comment.CommentRequestDto;
import com.study.totee.api.dto.mentor.MentorRequestDto;
import com.study.totee.api.dto.mentor.MentorResponseDto;
import com.study.totee.api.service.MentorService;
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
public class MentorController {

    private final MentorService mentorService;

    @ApiOperation(value = "멘토 등록하기", notes = "멘토에 지원합니다.")
    @PostMapping("/api/v1/mentor/apply")
    public ApiResponse<Object> applyMentor(@AuthenticationPrincipal User principal , @Valid @RequestBody MentorRequestDto mentorRequestDto){
        // 로그인이 되어 있지 않으면 예외를 던진다.
        String id = Optional.ofNullable(principal).orElseThrow(()->
                new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();

        mentorService.applyMentor(id, mentorRequestDto);

        return ApiResponse.success("message","멘토 지원에 성공하였습니다.");
    }

    @ApiOperation(value = "멘토 지원자보기", notes = "kind에 따라 분류됩니다. all = 전체 지원자, pending = 승인 대기중인 지원자" +
            " approved = 승인된 지원자")
    @GetMapping("/api/v1/mentor/list/{kind}")
    public ApiResponse<Object> appliedMentorList(@PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable String kind){
        Page<MentorResponseDto> mentorResponseDtoPage = mentorService.appliedMentorList(pageable, kind);

        return ApiResponse.success("data", mentorResponseDtoPage);
    }

}
