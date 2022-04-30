package com.study.totee.controller;

import com.study.totee.dto.ApiResponse;
import com.study.totee.dto.CommentDTO;
import com.study.totee.dto.PostDTO;
import com.study.totee.model.CommentEntity;
import com.study.totee.model.PostEntity;
import com.study.totee.model.UserEntity;
import com.study.totee.service.CommentService;
import com.study.totee.service.PostService;
import com.study.totee.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    @ApiOperation(value = "댓글 등록", notes = "댓글을 등록합니다")
    @PostMapping("/api/v1/comment")
    public ApiResponse saveComment(@AuthenticationPrincipal String id, @RequestBody CommentDTO commentDTO) {
        Optional<UserEntity> user = userService.getUserId(id);
        PostEntity post = postService.findByPostId(commentDTO.getPostId());
        CommentEntity commentEntity = CommentEntity.builder()
                .content(commentDTO.getContent())
                .user(user.get())
                .username(user.get().getUsername())
                .post(post)
                .build();
        commentService.save(commentEntity);
        return ApiResponse.success("message" , "SUCCESS");
    }

    @ApiOperation(value = "댓글 수정", notes = "댓글을 수정합니다")
    @PutMapping("/api/v1/comment")
    public ApiResponse updateComment(@AuthenticationPrincipal String id, @RequestBody CommentDTO commentDTO){
        commentService.update(commentDTO, id);
        return ApiResponse.success("message", "SUCCESS");
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글을 삭제합니다")
    @DeleteMapping("/api/v1/comment/{commentId}")
    public ApiResponse deleteComment(@AuthenticationPrincipal String id, @PathVariable Long commentId){
        commentService.delete(commentId, id);
        return ApiResponse.success("message", "SUCCESS");
    }
}