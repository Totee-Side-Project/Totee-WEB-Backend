package com.study.totee.api.persistence;

import com.study.totee.api.model.Applicant;
import com.study.totee.api.model.Post;
import com.study.totee.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    Optional<Applicant> findByUserAndPost(User user, Post post);
    List<Applicant> findAllByPost(Post post);
}
