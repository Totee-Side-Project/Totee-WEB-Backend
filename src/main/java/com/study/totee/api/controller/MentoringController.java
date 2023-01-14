package com.study.totee.api.controller;

import com.study.totee.api.dto.mentoring.MentoringRequestDto;
import com.study.totee.api.dto.mentoring.MentoringResponseDto;
import com.study.totee.api.dto.post.PostResponseDto;
import com.study.totee.api.service.MentoringService;
import com.study.totee.common.ApiResponse;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.NoAuthException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @ApiOperation(value = "멘토링 글 쓰기" , notes = "멘토링 게시글을 등록합니다.")
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

        MentoringResponseDto mentoringResponseDto = new MentoringResponseDto(mentoringService.findByMentoringId(mentoringId));
        return ApiResponse.success("data", mentoringResponseDto);
    }

    @ApiOperation(value = "전체 멘토링 글 목록 불러오기",
            notes = "ex : api/v1/post/list?page=0&size=5&sort=postId.desc")
    @GetMapping("/api/v1/mentoring/list")
    public ApiResponse<Object> findPostAll(@RequestParam(value = "kw", defaultValue = "") String kw, @PageableDefault(size = 16 , sort = "id",direction = Sort.Direction.DESC ) Pageable pageable){
        Page<MentoringResponseDto> page = mentoringService.findAllByTitleContaining(pageable, kw);

        return ApiResponse.success("data", page);
    }

    @ApiOperation(value = "멘토링 게시물 제목 검색합니다.", notes = "제목에 해당하는 게시글을 조회합니다. 빈 검색어 보내면 안됩니다")
    @GetMapping("/api/v2/mentoring/search/{title}")
    public ApiResponse<Object> findPostAllByTitle(@PathVariable String title, @PageableDefault
            (size = 16, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<MentoringResponseDto> page = mentoringService.findAllByTitleContaining(pageable, title);

        return ApiResponse.success("data", page);
    }

}
