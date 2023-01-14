package com.study.totee.api.persistence;

import com.study.totee.api.model.Mentoring;
import com.study.totee.api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MentoringRepository extends JpaRepository<Mentoring, Long> {

    void deleteByUser(User user);
    Page<Mentoring> findAll(Pageable pageable);
    Page<Mentoring> findAllByTitleContaining(String keyword, Pageable pageable);
}
