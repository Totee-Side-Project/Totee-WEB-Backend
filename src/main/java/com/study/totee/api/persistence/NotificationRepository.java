package com.study.totee.api.persistence;

import com.study.totee.api.model.Notification;
import com.study.totee.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findByIdAndUser(Long notificationId, User user);
    Notification findByPostId(Long postId);
    void deleteByPost_Id(Long postId);
    void deleteAllByPost_Id(Long postId);

    List<Notification> findAllByUser(User user);
}
