package com.study.totee.api.controller;

import com.study.totee.api.dto.post.PostResponseDto;
import com.study.totee.common.ApiResponse;
import com.study.totee.api.model.PostEntity;
import com.study.totee.api.service.LikeService;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.NoAuthException;
import com.study.totee.utils.PositionConverter;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    private final PositionConverter positionConverter;

    @ApiOperation(value = "좋아요!", notes = "이미 좋아요 되어있으면 좋아요는 취소됩니다.")
    @PostMapping("/api/v1/post/like/{postId}")
    public ApiResponse like(@AuthenticationPrincipal User principal , @PathVariable Long postId){
        // 로그인이 되어 있지 않으면 예외를 던진다.
        String id = Optional.ofNullable(principal).orElseThrow(()->
                new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();
        likeService.like(id, postId);

        return ApiResponse.success("message","게시글에 좋아요를 눌렀습니다.");
    }

    @ApiOperation(value = "좋아요한 글 리스트", notes = "로그인 한 유저의 좋아요 누른 글의 리스트를 조회합니다")
    @GetMapping("/api/v1/post/like")
    public ApiResponse myLikePost(@AuthenticationPrincipal User principal, @PageableDefault
            (size = 16, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable){
        Page<PostEntity> page = likeService.findAllByLikedPost(principal.getUsername(), pageable);

        Page<PostResponseDto> map = page.map(post -> new PostResponseDto(post.getPostId(), post.getTitle(), post.getContent(),
                post.getUser().getUserInfo().getNickname(), post.getView(), post.getLikeNum(), post.getCommentNum(),
                null, post.getUser().getProfileImageUrl(), post.getCreated_at(), post.getOnlineOrOffline(), post.getPeriod(),
                post.getStatus(), post.getCategory().getCategoryName(), positionConverter.convertPositionEntityToString(post.getPositionList()), post.getRecruitNum(), post.getContactMethod(),
                post.getContactLink(), post.getUser().getUserInfo().getPosition()));


        return ApiResponse.success("data", map);
    }

    @ApiOperation(value = "좋아요 여부", notes = "해당 포스트에 내가 '좋아요'를 눌렀는 지 확인합니다. 로그인이 안된 유저는 무조건 false를 반환합니다.")
    @GetMapping("/api/v1/post/isLike/{postId}")
    public ApiResponse isLike(@AuthenticationPrincipal User principal, @PathVariable Long postId){
        if (principal.getUsername() == null) {
            return ApiResponse.success("data", false);
        }
        boolean isLike = likeService.isLike(principal.getUsername(), postId);
        return ApiResponse.success("data", isLike);
    }
}
