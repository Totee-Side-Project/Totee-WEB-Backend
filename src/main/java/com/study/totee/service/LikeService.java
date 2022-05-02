package com.study.totee.service;

import com.study.totee.model.LikeEntity;
import com.study.totee.model.PostEntity;
import com.study.totee.model.UserEntity;
import com.study.totee.persistence.LikeRepository;
import com.study.totee.persistence.PostRepository;
import com.study.totee.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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
            Optional<UserEntity> userEntity = userRepository.findById(userId);
            LikeEntity newLike = new LikeEntity();
            newLike.setUser(userEntity.get());
            newLike.setPost(post);
            likeRepository.save(newLike);
            log.info( userId + " like " + postId);
        }else {
            likeRepository.delete(like);
            log.info( userId + " dislike " + postId);
        }

    }

    public Page<PostEntity> findAllByLikedPost(String userId, final Pageable pageable){
        Optional<UserEntity> user = userRepository.findById(userId);
        Page<PostEntity> likeEntityList = likeRepository.findAllByLikedPost(user.get(), pageable);
        return likeEntityList;
    }
}
