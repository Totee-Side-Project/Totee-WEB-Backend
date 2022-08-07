package com.study.totee.api.persistence;


import com.study.totee.api.model.Reply;
import com.study.totee.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findReplyEntityByComment_Id(Long id);
    Reply findByIdAndUser(Long replyId, User user);
}
