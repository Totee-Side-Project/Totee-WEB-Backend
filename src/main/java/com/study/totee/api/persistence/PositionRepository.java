package com.study.totee.api.persistence;

import com.study.totee.api.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PositionRepository extends JpaRepository<Position, Long> {

    @Modifying
    @Query(value = "DELETE FROM Position p WHERE p.post.id = ?1")
    void deleteAllByPostId(Long postId);
}
