package com.study.totee.api.persistence;

import com.study.totee.api.model.Comment;
import com.study.totee.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByIdAndUser(Long id, User user);
    List<Comment> findAllByPost_Id(Long postId);
}
