package com.study.totee.api.service;

import com.study.totee.api.dto.comment.CommentRequestDto;
import com.study.totee.api.model.CommentEntity;
import com.study.totee.api.model.PostEntity;
import com.study.totee.api.model.UserEntity;
import com.study.totee.api.persistence.CommentRepository;
import com.study.totee.api.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostService postService;

    @Transactional
    public void save(CommentRequestDto commentRequestDto, String userId){
        UserEntity user = userRepository.findById(userId);
        PostEntity post = postService.findByPostId(commentRequestDto.getPostId());

        CommentEntity commentEntity = CommentEntity.builder()
                .content(commentRequestDto.getContent())
                .user(user)
                .username(user.getUsername())
                .post(post)
                .build();
        post.setCommentNum(post.getCommentNum() + 1);
        post.getComment().add(commentEntity);
        commentRepository.save(commentEntity);
    }

    @Transactional
    public void update(CommentRequestDto commentRequestDto, String userId, Long commentId){
        PostEntity post = postService.findByPostId(commentRequestDto.getPostId());
        UserEntity user = userRepository.findById(userId);
        CommentEntity commentEntity = commentRepository.findByCommentIdAndUser(commentId, user);
        post.setCommentNum(post.getCommentNum() - 1);
        commentEntity.setContent(commentRequestDto.getContent());
    }

    @Transactional
    public void delete(Long commentId, String userId){
        UserEntity user = userRepository.findById(userId);
        CommentEntity commentEntity = commentRepository.findByCommentIdAndUser(commentId, user);
        PostEntity post = postService.findByPostId(commentEntity.getPost().getPostId());
        post.setCommentNum(post.getCommentNum() - 1);
        commentRepository.delete(commentEntity);
    }

    @Transactional(readOnly = true)
    public List<CommentEntity> CommentListByPostId(Long postId){
        List<CommentEntity> commentEntities = commentRepository.findCommentEntityByPost_PostId(postId);
        return commentEntities;
    }
}
