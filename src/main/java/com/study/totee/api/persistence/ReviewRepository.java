package com.study.totee.api.persistence;

import com.study.totee.api.model.Mentoring;
import com.study.totee.api.model.Reply;
import com.study.totee.api.model.Review;
import com.study.totee.api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUserAndMentoring(User user, Mentoring mentoring);
    Page<Review> findAllByMentoring(Pageable pageable, Mentoring mentoring);
}
