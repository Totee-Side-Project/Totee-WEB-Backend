package com.study.totee.persistence;

import com.study.totee.model.LikeEntity;
import com.study.totee.model.PostEntity;
import com.study.totee.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LikeRepository extends JpaRepository<LikeEntity,Integer> {
    LikeEntity findByUser_IdAndPost_PostId(String userId, Long postId);
    @Query(value = "SELECT p FROM PostEntity p LEFT JOIN LikeEntity l " +
            "ON l.post.postId = p.postId WHERE l.user = ?1")
    Page<PostEntity> findAllByLikedPost(UserEntity user, Pageable pageable);
}
