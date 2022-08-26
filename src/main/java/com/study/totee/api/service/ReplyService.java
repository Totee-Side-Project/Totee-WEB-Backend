package com.study.totee.api.service;

import com.study.totee.api.dto.reply.ReplyRequestDto;
import com.study.totee.api.model.*;
import com.study.totee.api.persistence.*;
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
    private final NotificationRepository notificationRepository;

    @Transactional
    public void save(ReplyRequestDto replyRequestDto, String userId) {
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        Comment comment = commentRepository.findById(replyRequestDto.getCommentId())
                .orElseThrow(() -> new BadRequestException(ErrorCode.NO_COMMENT_ERROR));

        postRepository.findById(comment.getPost().getId()).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_POST_ERROR));

        Reply replyEntity = new Reply(user, comment, replyRequestDto);
        comment.addReply(replyEntity);

        if(!comment.getUser().getId().equals(userId)) {
            Notification notification = new Notification(comment, user);
            notificationRepository.save(notification);
        }

        replyRepository.save(replyEntity);
    }

    @Transactional
    public void update(ReplyRequestDto replyRequestDto, String userId, Long replyId) {
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        Comment comment = commentRepository.findById(replyRequestDto.getCommentId())
                .orElseThrow(() -> new BadRequestException(ErrorCode.NO_COMMENT_ERROR));

        postRepository.findById(comment.getPost().getId()).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_POST_ERROR));

        Reply replyEntity = Optional.ofNullable(replyRepository.findByIdAndUser(replyId, user)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_REPLY_ERROR));

        replyEntity.setContent(replyRequestDto.getContent());
    }

    @Transactional
    public void delete(Long replyId, String userId){
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        Reply reply = Optional.ofNullable(replyRepository.findByIdAndUser(replyId, user)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_REPLY_ERROR));

        Comment comment = commentRepository.findById(reply.getComment().getId())
                .orElseThrow(() -> new BadRequestException(ErrorCode.NO_COMMENT_ERROR));

        Post post = postRepository.findById(comment.getPost().getId()).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_POST_ERROR));

        post.decreaseCommentNum();
        replyRepository.delete(reply);
    }
}