package com.study.totee.api.persistence;

import com.study.totee.api.model.CommentEntity;
import com.study.totee.api.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    CommentEntity findByCommentIdAndUser(Long commentId, UserEntity user);
    List<CommentEntity> findCommentEntityByPost_PostId(Long postId);
}
