package com.study.totee.api.controller;

import com.study.totee.api.dto.mentoring.MentoringRequestDto;
import com.study.totee.api.dto.mentoring.MentoringResponseDto;
import com.study.totee.api.dto.post.PostRequestDto;
import com.study.totee.api.dto.post.PostResponseDto;
import com.study.totee.api.service.MentoringService;
import com.study.totee.common.ApiResponse;
import com.study.totee.common.ResponseDto;
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
    public ApiResponse<Object> create(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, @Valid @RequestBody MentoringRequestDto requestDto) throws IOException {
        // 로그인 정보가 없으면 예외 발생
        String id = Optional.ofNullable(principal).orElseThrow(() -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();

        mentoringService.save(id, requestDto);

        return ApiResponse.success("message" , "멘토링 게시글이 성공적으로 등록되었습니다.");
    }

    @ApiOperation(value = "멘토링 게시글 업데이트", notes = "게시글을 수정합니다")
    @PutMapping("/api/v1/mentoring/{mentoringId}")
    public ApiResponse<Object> update(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, @RequestBody @Valid MentoringRequestDto mentoringRequestDto, @PathVariable Long mentoringId) throws IOException {
        // 로그인 정보가 없으면 예외 발생
        String id = Optional.ofNullable(principal).orElseThrow(() -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();

        mentoringService.update(id, mentoringRequestDto, mentoringId);

        return ApiResponse.success("message", "게시글이 성공적으로 업데이트되었습니다.");
    }

    @ApiOperation(value = "멘토링 게시글 삭제" , notes = "게시글을 삭제합니다")
    @DeleteMapping("/api/v1/mentoring/{mentoringId}")
    public ApiResponse<Object> deletePost(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, @PathVariable Long mentoringId) {
        // 로그인 정보가 없으면 예외 발생
        String id = Optional.ofNullable(principal).orElseThrow(() -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();

        mentoringService.delete(mentoringId, id);

        return ApiResponse.success("message", "게시글이 성공적으로 삭제되었습니다.");
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
    public ApiResponse<Object> findPostAll(@RequestParam(value = "kw", defaultValue = "") String kw, @PageableDefault(size = 20 , sort = "id",direction = Sort.Direction.DESC ) Pageable pageable){
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

    @ApiOperation(value = "내가 참여중인 멘토링 글 리스트", notes = "내가 참여중인 멘토링 글 리스트를 조회합니다.")
    @GetMapping("/api/v1/mentoring/myMentoring")
    public ApiResponse<Object> findAllByMyStudyTeam(@PageableDefault(size = 20 , sort = "id",direction = Sort.Direction.DESC ) Pageable pageable,
                                                    @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal){
        // 로그인 정보가 없으면 예외 발생
        String id = Optional.ofNullable(principal).orElseThrow(() -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();

        Page<MentoringResponseDto> page = mentoringService.findAllByMyMentoringTeam(pageable, id);

        return ApiResponse.success("data", page);
    }

    @ApiOperation(value = "좋아요한 멘토링 글 리스트", notes = "로그인 한 유저의 좋아요 누른 글의 리스트를 조회합니다")
    @GetMapping("/api/v1/mentoring/like")
    public ApiResponse<Object> myLikePost(@PageableDefault(size = 20 , sort = "id",direction = Sort.Direction.DESC ) Pageable pageable,
                                          @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal){
        // 로그인 정보가 없으면 예외 발생
        String id = Optional.ofNullable(principal).orElseThrow(() -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();

        Page<MentoringResponseDto> page = mentoringService.findAllByLikedPost(id, pageable);

        return ApiResponse.success("data", page);
    }
}
