package com.study.totee.api.persistence;

import com.study.totee.api.model.Mentor;
import com.study.totee.api.model.Post;
import com.study.totee.api.model.User;
import com.study.totee.type.PositionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MentorRepository extends JpaRepository<Mentor, Long> {
    boolean existsByUser(User user);
    Page<Mentor> findAll(Pageable pageable);
}
