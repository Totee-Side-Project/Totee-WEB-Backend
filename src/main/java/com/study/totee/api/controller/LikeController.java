package com.study.totee.api.controller;

import com.study.totee.api.dto.post.PostResponseDto;
import com.study.totee.api.model.UserEntity;
import com.study.totee.common.ApiResponse;
import com.study.totee.api.model.PostEntity;
import com.study.totee.api.service.LikeService;
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

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @ApiOperation(value = "좋아요!", notes = "이미 좋아요 되어있으면 좋아요는 취소됩니다.")
    @PostMapping("/api/v1/post/like/{postId}")
    public ApiResponse like(@AuthenticationPrincipal User principal , @PathVariable Long postId){
        likeService.like(principal.getUsername(), postId);

        return ApiResponse.success("message","SUCCESS");
    }

    @ApiOperation(value = "좋아요한 글 리스트", notes = "좋아요 누른 글의 리스트를 조회합니다")
    @GetMapping("/api/v1/post/like")
    public ApiResponse myLikePost(@AuthenticationPrincipal User principal, @PageableDefault
            (size = 16, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable){
        Page<PostEntity> page = likeService.findAllByLikedPost(principal.getUsername(), pageable);

        Page<PostResponseDto> map = page.map(post -> new PostResponseDto(post.getPostId(), post.getTitle(), post.getContent(),
                post.getUser().getUserInfo().getNickname(), post.getView(), post.getLikeNum(), post.getCommentNum(),
                null, post.getImageUrl(), post.getCreated_at(), post.getOnlineOrOffline(), post.getPeriod(),
                post.getTarget(), post.getStatus(), post.getCategory().getCategoryName()));


        return ApiResponse.success("data", map);
    }
}
