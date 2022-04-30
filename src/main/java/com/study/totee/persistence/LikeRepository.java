package com.study.totee.persistence;

import com.study.totee.model.LikeEntity;
import com.study.totee.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<LikeEntity,Integer> {
    LikeEntity findByUser_IdAndPost_PostId(String userId, Long postId);
    List<LikeEntity> findAllByUser(UserEntity user);
}
