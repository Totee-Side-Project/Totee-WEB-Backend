package com.study.totee.api.persistence;


import com.study.totee.api.model.ReplyEntity;
import com.study.totee.api.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {
    List<ReplyEntity> findReplyEntityByComment_CommentId(Long commentId);
    ReplyEntity findByIdAndUser(Long replyId, UserEntity user);
}
