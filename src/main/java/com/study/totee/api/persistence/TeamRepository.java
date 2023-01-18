package com.study.totee.api.persistence;

import com.study.totee.api.model.Mentoring;
import com.study.totee.api.model.Post;
import com.study.totee.api.model.Team;
import com.study.totee.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);
    void deleteAllByPost(Post post);
    void deleteAllByMentoring(Mentoring mentoring);
}
