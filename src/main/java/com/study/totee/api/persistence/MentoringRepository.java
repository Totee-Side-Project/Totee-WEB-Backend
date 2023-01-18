package com.study.totee.api.persistence;

import com.google.common.io.Files;
import com.study.totee.api.model.Mentoring;
import com.study.totee.api.model.Post;
import com.study.totee.api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MentoringRepository extends JpaRepository<Mentoring, Long> {

    void deleteByUser(User user);
    Page<Mentoring> findAll(Pageable pageable);
    Page<Mentoring> findAllByTitleContaining(String keyword, Pageable pageable);
    Mentoring findByIdAndUser(Long mentoringId, User user);

    @Query(value = "SELECT p FROM Mentoring p WHERE p.id IN (SELECT t.mentoring.id FROM Team t WHERE t.user = ?1)")
    Page<Mentoring> findAllByMyMentoringTeam(User user, Pageable pageable);

    @Query(value = "SELECT p FROM Mentoring p WHERE p.id IN (SELECT t.mentoring.id FROM Team t WHERE t.user = ?1)")
    Page<Mentoring> findAllByLikedMentoring(User user, Pageable pageable);

    Page<Mentoring> findAllByUser(Pageable pageable, User user);
}
