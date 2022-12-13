package com.study.totee.api.service;

import com.study.totee.api.dto.comment.CommentRequestDto;
import com.study.totee.api.dto.comment.CommentResponseDto;
import com.study.totee.api.dto.post.PostResponseDto;
import com.study.totee.api.model.Comment;
import com.study.totee.api.model.Notification;
import com.study.totee.api.model.Post;
import com.study.totee.api.model.User;
import com.study.totee.api.persistence.CommentRepository;
import com.study.totee.api.persistence.NotificationRepository;
import com.study.totee.api.persistence.PostRepository;
import com.study.totee.api.persistence.UserRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.study.totee.api.controller.NotificationController.sseEmitters;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public void save(CommentRequestDto commentRequestDto, String userId){
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));
        Post post = postRepository.findById(commentRequestDto.getPostId()).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));

        Comment commentEntity = commentRepository.save(new Comment(user, post, commentRequestDto));
        post.addComment(commentEntity);

        if (!post.getUser().getId().equals(userId)) {
            Notification notification = new Notification(post, user, commentEntity);
            notificationRepository.save(notification);
            if (sseEmitters.containsKey(post.getUser().getId())) {
                SseEmitter sseEmitter = sseEmitters.get(post.getUser().getId());
                try {
                    sseEmitter.send(SseEmitter.event().name("sse")
                            .data(user.getUserInfo().getNickname() + " 님이 " +
                                    post.getContent() + " 게시글에 댓글을 남기셨습니다!"));
                } catch (Exception e) {
                    sseEmitters.remove(user.getUserInfo().getNickname());
                }
            }
        }
    }

    @Transactional
    public void update(CommentRequestDto commentRequestDto, String userId, Long commentId){
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));
        postRepository.findById(commentRequestDto.getPostId()).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));

        Comment commentEntity = commentRepository.findByIdAndUser(commentId, user);
        commentEntity.setContent(commentRequestDto.getContent());
    }

    @Transactional
    public void delete(Long commentId, String userId){
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));
        Comment comment = Optional.ofNullable(commentRepository.findByIdAndUser(commentId, user)).
                orElseThrow(()-> new BadRequestException(ErrorCode.NO_COMMENT_ERROR));
        Post post = postRepository.findById(comment.getPost().getId()).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));

        Notification notification = notificationRepository.findByPostAndUserAndCommentId(post, post.getUser(), commentId);
        if(notification != null){
            notificationRepository.delete(notification);
        }
        post.decreaseCommentNum(comment);
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> commentListByPostId(Long postId){
        return commentRepository.findAllByPost_Id(postId).stream()
                .map(CommentResponseDto::new).collect(Collectors.toList());
    }
}