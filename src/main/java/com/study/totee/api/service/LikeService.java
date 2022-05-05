package com.study.totee.api.service;

import com.study.totee.api.model.LikeEntity;
import com.study.totee.api.model.PostEntity;
import com.study.totee.api.model.UserEntity;
import com.study.totee.api.persistence.LikeRepository;
import com.study.totee.api.persistence.PostRepository;
import com.study.totee.api.persistence.UserRepository;
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
        LikeEntity like = likeRepository.findByUser_IdAndPost_PostId(userId , postId);
        if(like == null){
            PostEntity post = postRepository.findByPostId(postId);
            UserEntity userEntity = userRepository.findById(userId);
            LikeEntity newLike = new LikeEntity();
            newLike.setUser(userEntity);
            newLike.setPost(post);
            likeRepository.save(newLike);
            log.info( userId + " like " + postId);
        }else {
            likeRepository.delete(like);
            log.info( userId + " dislike " + postId);
        }

    }

    public Page<PostEntity> findAllByLikedPost(String userId, final Pageable pageable){
        UserEntity user = userRepository.findById(userId);
        Page<PostEntity> likeEntityList = likeRepository.findAllByLikedPost(user, pageable);
        return likeEntityList;
    }
}
