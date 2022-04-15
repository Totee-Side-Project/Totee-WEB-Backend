package com.study.totee.persistence;

import com.study.totee.model.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity,Integer> {
    LikeEntity findByUser_IdAndPost_PostId(String userId, Long postId);
    // int countByPost_PostId(Long postId);
}
