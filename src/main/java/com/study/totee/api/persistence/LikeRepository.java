package com.study.totee.api.persistence;

import com.study.totee.api.model.Like;
import com.study.totee.api.model.Post;
import com.study.totee.api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByUserAndPost_Id(User user, Long postId);
}
