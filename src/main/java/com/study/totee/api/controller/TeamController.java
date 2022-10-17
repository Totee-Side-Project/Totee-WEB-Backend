package com.study.totee.api.controller;

import com.study.totee.api.dto.team.MemberListResponseDto;
import com.study.totee.api.dto.team.TeamRequestDto;
import com.study.totee.api.model.Post;
import com.study.totee.api.model.User;
import com.study.totee.api.service.PostService;
import com.study.totee.api.service.TeamService;
import com.study.totee.api.service.UserService;
import com.study.totee.common.ApiResponse;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.ForbiddenException;
import com.study.totee.exption.NoAuthException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;
    private final PostService postService;

    @CacheEvict(value = "Post", allEntries=true)
    @ApiOperation(value = "팀원 승인/거절")
    @PostMapping("/api/v1/team/{postId}")
    public ApiResponse<Object> acceptMember(@PathVariable Long postId,
                                               @RequestBody TeamRequestDto requestDto,
                                               @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal){
        // [예외처리] 로그인 정보가 없을 때
        String userId = Optional.ofNullable(principal.getUsername()).orElseThrow(
                () -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)
        );

        //로그인 사용자 정보 불러오기
        User user = userService.getUser(userId);
        //로그인 사용자가 해당 프로젝트의 생성자 인지 확인
        Post post = postService.loadPostIfOwner(postId, user);
        //지원자 정보 확인
        User applyUser = userService.loadUserByUserId(123L);
        //지원자 승인/거절
        teamService.AcceptApplication(post, applyUser, requestDto.isAccept());
        //팀원 목록 출력
        List<MemberListResponseDto> responseDto = teamService.getMember(postId);
        return ApiResponse.success("data" , responseDto);
    }

    @ApiOperation(value = "팀원 조회")
    @GetMapping("/api/v1/team/{postId}")
    public ApiResponse<Object> getMember(@PathVariable Long postId) {
        List<MemberListResponseDto> responseDto = teamService.getMember(postId);
        return ApiResponse.success("data" , responseDto);
    }

    @CacheEvict(value = "Post", allEntries=true)
    @ApiOperation(value = "팀원 강퇴")
    @DeleteMapping("/api/v1/team/resignation/{postId}")
    public ApiResponse<Object> memberResignation(@RequestParam Long targetId,
                                                    @PathVariable Long postId,
                                                    @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        Post post = postService.findByPostId(postId);
        String userId = Optional.ofNullable(principal.getUsername()).orElseThrow(
                () -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)
        );
        User user = userService.getUser(userId);

        if (post.getUser().getId().equals(user.getId())) {
            User member = userService.loadUserByUserId(targetId);
            teamService.memberDelete(member, post);
            return ApiResponse.success("message" , "팀원을 강퇴했습니다.");
            // [예외처리] 팀원 강퇴를 요청한 사용자가 게시물 작성자가 아닐 때
        } else throw new ForbiddenException(ErrorCode.NO_AUTHORIZATION_ERROR);
    }

    @CacheEvict(value = "Post", allEntries=true)
    @ApiOperation(value = "팀 탈퇴")
    @DeleteMapping("/api/v1/team/secession/{postId}")
    public ApiResponse<Object> memberSecession(@PathVariable Long postId,
                                                  @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        Post post = postService.findByPostId(postId);
        String userId = Optional.ofNullable(principal.getUsername()).orElseThrow(
                () -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)
        );
        User user = userService.getUser(userId);
        teamService.memberDelete(user, post);
        return ApiResponse.success("message" , "해당 팀에서 탈퇴되었습니다.");
    }
}
