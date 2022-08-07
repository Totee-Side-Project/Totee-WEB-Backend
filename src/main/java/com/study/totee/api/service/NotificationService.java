package com.study.totee.api.service;

import com.study.totee.api.dto.notification.NotificationResponseDto;
import com.study.totee.api.model.Notification;
import com.study.totee.api.model.User;
import com.study.totee.api.persistence.NotificationRepository;
import com.study.totee.api.persistence.UserRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<NotificationResponseDto> notificationDtoList(String userId) {
        final User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_USER_ERROR));

        return notificationRepository.findAllByUser(user).stream()
                .map(NotificationResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificationResponseDto readNotification(String userId, Long notificationId) {
        final User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_USER_ERROR));

        final Notification notification = Optional.ofNullable(notificationRepository.findByIdAndUser(notificationId, user)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_NOTIFICATION_ERROR));

        notification.setIsRead("Y");

        return new NotificationResponseDto(notification);
    }

    @Transactional
    public NotificationResponseDto deleteNotification(String userId, Long notificationId) {
        final User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_USER_ERROR));

        final Notification notification = Optional.ofNullable(notificationRepository.findByIdAndUser(notificationId, user)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_NOTIFICATION_ERROR));

        notificationRepository.delete(notification);

        return new NotificationResponseDto(notification);
    }
}
