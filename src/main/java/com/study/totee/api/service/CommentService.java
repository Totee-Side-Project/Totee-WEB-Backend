package com.study.totee.api.service;

import com.study.totee.api.dto.comment.CommentRequestDto;
import com.study.totee.api.model.CommentEntity;
import com.study.totee.api.model.PostEntity;
import com.study.totee.api.model.UserEntity;
import com.study.totee.api.persistence.CommentRepository;
import com.study.totee.api.persistence.PostRepository;
import com.study.totee.api.persistence.UserRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.ForbiddenException;
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
    private final PostRepository postRepository;

    @Transactional
    public void save(CommentRequestDto commentRequestDto, String userId){
        UserEntity user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_USER_ERROR));
        PostEntity post = Optional.ofNullable(postRepository.findByPostId(commentRequestDto.getPostId())).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));

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
        UserEntity user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_USER_ERROR));
        PostEntity post = Optional.ofNullable(postRepository.findByPostId(commentRequestDto.getPostId())).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));

        CommentEntity commentEntity = commentRepository.findByCommentIdAndUser(commentId, user);
        commentEntity.setContent(commentRequestDto.getContent());
    }

    @Transactional
    public void delete(Long commentId, String userId){
        UserEntity user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_USER_ERROR));
        PostEntity post = Optional.ofNullable(postRepository.findByPostId(commentId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));
        CommentEntity commentEntity = Optional.ofNullable(commentRepository.findByCommentIdAndUser(commentId, user)).
                orElseThrow(()-> new BadRequestException(ErrorCode.NO_COMMENT_ERROR));

        post.setCommentNum(post.getCommentNum() - 1);
        commentRepository.delete(commentEntity);
    }

    @Transactional(readOnly = true)
    public List<CommentEntity> commentListByPostId(Long postId){
        List<CommentEntity> commentEntities = commentRepository.findCommentEntityByPost_PostId(postId);
        return commentEntities;
    }
}