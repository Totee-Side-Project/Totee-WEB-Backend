package com.study.totee.api.service;

import com.study.totee.api.dto.reply.ReplyRequestDto;
import com.study.totee.api.model.CommentEntity;
import com.study.totee.api.model.PostEntity;
import com.study.totee.api.model.ReplyEntity;
import com.study.totee.api.model.UserEntity;
import com.study.totee.api.persistence.CommentRepository;
import com.study.totee.api.persistence.PostRepository;
import com.study.totee.api.persistence.ReplyRepository;
import com.study.totee.api.persistence.UserRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyService {

    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void save(ReplyRequestDto replyRequestDto, String userId) {
        UserEntity user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_USER_ERROR));
        CommentEntity comment = Optional.ofNullable(commentRepository.findByCommentId(replyRequestDto.getCommentId()))
                .orElseThrow(() -> new BadRequestException(ErrorCode.NO_COMMENT_ERROR));
        PostEntity post = Optional.ofNullable(postRepository.findByPostId(comment.getPost().getPostId())).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_POST_ERROR));

        ReplyEntity replyEntity = ReplyEntity.builder()
                .content(replyRequestDto.getContent())
                .profileImageUrl(user.getProfileImageUrl())
                .user(user)
                .nickname(user.getUserInfo().getNickname())
                .comment(comment)
                .build();

        comment.getReply().add(replyEntity);
        post.setCommentNum(post.getCommentNum() + 1);
        replyRepository.save(replyEntity);
    }

    @Transactional
    public void update(ReplyRequestDto replyRequestDto, String userId, Long replyId) {
        UserEntity user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_USER_ERROR));
        CommentEntity comment = Optional.ofNullable(commentRepository.findByCommentId(replyRequestDto.getCommentId()))
                .orElseThrow(() -> new BadRequestException(ErrorCode.NO_COMMENT_ERROR));
        PostEntity post = Optional.ofNullable(postRepository.findByPostId(comment.getPost().getPostId())).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_POST_ERROR));
        ReplyEntity replyEntity = Optional.ofNullable(replyRepository.findByIdAndUser(replyId, user)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_REPLY_ERROR));

        replyEntity.setContent(replyRequestDto.getContent());
    }

    @Transactional
    public void delete(Long replyId, String userId){
        UserEntity user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_USER_ERROR));
        ReplyEntity reply = Optional.ofNullable(replyRepository.findByIdAndUser(replyId, user)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_REPLY_ERROR));
        CommentEntity comment = Optional.ofNullable(commentRepository.findByCommentId(reply.getComment().getCommentId()))
                .orElseThrow(() -> new BadRequestException(ErrorCode.NO_COMMENT_ERROR));
        PostEntity post = Optional.ofNullable(postRepository.findByPostId(comment.getPost().getPostId())).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_POST_ERROR));

        post.setCommentNum(post.getCommentNum() - 1);
        replyRepository.delete(reply);
    }

    @Transactional(readOnly = true)
    public List<ReplyEntity> replyListByCommentId(Long commentId){
        return replyRepository.findReplyEntityByComment_CommentId(commentId);
    }
}