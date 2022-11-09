package com.study.totee.api.service;

import com.study.totee.api.model.Like;
import com.study.totee.api.model.Notification;
import com.study.totee.api.model.Post;
import com.study.totee.api.model.User;
import com.study.totee.api.persistence.LikeRepository;
import com.study.totee.api.persistence.NotificationRepository;
import com.study.totee.api.persistence.PostRepository;
import com.study.totee.api.persistence.UserRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public void like(String userId, Long postId){
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));

        Like like = likeRepository.findByUserAndPost(user , post);
        if(like == null){
            post.increaseLikeNum();
            Like savedLike = likeRepository.save(new Like(user, post));
            if (!post.getUser().getId().equals(userId)) {
                notificationRepository.save(new Notification(savedLike, user));
            }
        }else {
            Notification notification = notificationRepository.findByPostAndUserAndLikeId(post, user, like.getId());
            notificationRepository.delete(notification);
            post.decreaseLikeNum();
            likeRepository.delete(like);
        }

    }

    @Transactional(readOnly = true)
    public boolean isLike(String userId, Long postId){
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));
        postRepository.findById(postId).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));

        Like like = likeRepository.findByUserAndPost_Id(user, postId);
        return like != null;
    }

}
