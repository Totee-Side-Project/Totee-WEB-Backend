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
        PostEntity post = postRepository.findByPostId(postId);
        UserEntity userEntity = getUser(userId);
        // 포스트가 존재하지 않거나 로그인이 되어 있지 않으면 예외를 던짐
        if (post == null) {
            log.error("Can not find post by postId: {}", postId);
            throw new ForbiddenException(ErrorCode.NO_POST_ERROR);
        }
        else if (userEntity == null){
            throw new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR);
        }

        LikeEntity like = likeRepository.findByUser_IdAndPost_PostId(userId , postId);
        if(like == null){
            LikeEntity newLike = new LikeEntity();
            post.setLikeNum(post.getLikeNum() + 1);
            newLike.setUser(userEntity);
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
        PostEntity post = postRepository.findByPostId(postId);
        if (post == null) {
            log.error("Can not find post by postId: {}", postId);
            throw new ForbiddenException(ErrorCode.NO_POST_ERROR);
        }
        LikeEntity like = likeRepository.findByUser_IdAndPost_PostId(userId , postId);
        return like != null;
    }

    public Page<PostEntity> findAllByLikedPost(String userId, final Pageable pageable){
        UserEntity user = getUser(userId);
        Page<PostEntity> likeEntityList = likeRepository.findAllByLikedPost(user, pageable);
        return likeEntityList;
    }

    public UserEntity getUser(String userId){
        return userRepository.findById(userId);
    }
}
