package com.study.totee.api.service;

import com.study.totee.api.model.LikeEntity;
import com.study.totee.api.model.PostEntity;
import com.study.totee.api.model.UserEntity;
import com.study.totee.api.persistence.LikeRepository;
import com.study.totee.api.persistence.PostRepository;
import com.study.totee.api.persistence.UserRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.ForbiddenException;
import com.study.totee.exption.NoAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public void like(String userId, Long postId){
        UserEntity user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_USER_ERROR));
        PostEntity post = Optional.ofNullable(postRepository.findByPostId(postId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));

        LikeEntity like = likeRepository.findByUser_IdAndPost_PostId(userId , postId);
        if(like == null){
            LikeEntity newLike = new LikeEntity();
            post.setLikeNum(post.getLikeNum() + 1);
            newLike.setUser(user);
            newLike.setPost(post);
            likeRepository.save(newLike);
            log.info( userId + " like " + postId);
        }else {
            post.setLikeNum(post.getLikeNum() - 1);
            likeRepository.delete(like);
            log.info( userId + " dislike " + postId);
        }

    }

    public boolean isLike(String userId, Long postId){
        PostEntity post = Optional.ofNullable(postRepository.findByPostId(postId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));

        LikeEntity like = likeRepository.findByUser_IdAndPost_PostId(userId , postId);
        return like != null;
    }

    public Page<PostEntity> findAllByLikedPost(String userId, final Pageable pageable){
        UserEntity user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_USER_ERROR));

        Page<PostEntity> likeEntityList = likeRepository.findAllByLikedPost(user, pageable);
        return likeEntityList;
    }

}
