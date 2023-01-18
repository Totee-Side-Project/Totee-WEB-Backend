package com.study.totee.api.persistence;

import com.study.totee.api.model.Mentoring;
import com.study.totee.api.model.Notification;
import com.study.totee.api.model.Post;
import com.study.totee.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findByIdAndUser(Long notificationId, User user);
    void deleteAllByPost_Id(Long postId);

    List<Notification> findAllByUserOrderByIdDesc(User user);
    Notification findByPostAndUserAndLikeId(Post post, User user, Long likeId);
    Notification findByPostAndUserAndReplyId(Post post, User user, Long replyId);
    Notification findByPostAndUserAndCommentId(Post post, User user, Long commentId);

    Notification findByMentoringAndUserAndLikeId(Mentoring mentoring, User user, Long id);
}
