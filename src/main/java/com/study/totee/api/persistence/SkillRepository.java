package com.study.totee.api.persistence;

import com.study.totee.api.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    @Modifying
    @Query(value = "DELETE FROM Skill s WHERE s.post.id = ?1")
    void deleteAllByPostId(Long postId);
}
