package com.study.totee.controller;

import com.study.totee.dto.ApiResponse;
import com.study.totee.service.LikeService;
import com.study.totee.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @ApiOperation(value = "좋아요!", notes = "좋아요 있으면 delete 없으면 없애기")
    @PostMapping("/api/v1/post/{postId}/like")
    public ApiResponse like(@AuthenticationPrincipal String id , @PathVariable Long postId){
        likeService.like(id, postId);
        return ApiResponse.success("message","SUCCESS");
    }
}
