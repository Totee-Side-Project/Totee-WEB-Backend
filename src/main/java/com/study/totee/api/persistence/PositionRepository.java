package com.study.totee.api.persistence;

import com.study.totee.api.model.PositionEntity;
import com.study.totee.api.model.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PositionRepository extends JpaRepository<PositionEntity, Long> {

    @Modifying
    @Query(value = "DELETE FROM PositionEntity p WHERE p.post.postId = ?1")
    void deleteAllByPostId(Long postId);
}
