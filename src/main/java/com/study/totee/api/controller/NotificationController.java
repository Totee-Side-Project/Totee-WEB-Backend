package com.study.totee.api.controller;

import com.study.totee.api.dto.notification.NotificationResponseDto;
import com.study.totee.api.model.User;
import com.study.totee.api.service.NotificationService;
import com.study.totee.api.service.UserService;
import com.study.totee.common.ApiResponse;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.NoAuthException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    public static Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private final UserService userService;

    @ApiOperation(value = "알림 목록 조회", notes = "알림 목록을 조회합니다.")
    @GetMapping("/api/v1/notification")
    public ApiResponse<Object> getNotificationList(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        String id = Optional.ofNullable(principal).orElseThrow(() -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();
        List<EntityModel<NotificationResponseDto>> notifications = notificationService.notificationDtoList(id).stream()
                .map(notification -> EntityModel.of(notification,
                        linkTo(methodOn(NotificationController.class).readNotification(principal, notification.getNotificationId())).withRel("read"),
                        linkTo(methodOn(NotificationController.class).deleteNotification(principal, notification.getNotificationId())).withRel("delete"),
                        linkTo(methodOn(PostController.class).getPost(notification.getPostId(), null, null)).withRel("post")))
                .collect(Collectors.toList());

        return ApiResponse.success("data", notifications);
    }

    @ApiOperation(value = "알림 읽음 처리", notes = "알림을 읽음 처리합니다.")
    @PostMapping("/api/v1/notification/{notificationId}")
    public ResponseEntity<Object> readNotification(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,@PathVariable Long notificationId) {
        String id = Optional.ofNullable(principal).orElseThrow(() -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();

        return ResponseEntity.status(HttpStatus.OK).body(notificationService.readNotification(id, notificationId));
    }

    @ApiOperation(value = "알림 삭제", notes = "알림을 삭제합니다.")
    @DeleteMapping("/api/v1/notification/{notificationId}")
    public ResponseEntity<Object> deleteNotification(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,@PathVariable Long notificationId) {
        String id = Optional.ofNullable(principal).orElseThrow(() -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();

        return ResponseEntity.status(HttpStatus.OK).body(notificationService.deleteNotification(id, notificationId));
    }

    /**
     * @title 로그인 한 유저 sse 연결
     */
    @GetMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        // 현재 클라이언트를 위한 SseEmitter 생성
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            // 연결!!
            sseEmitter.send(SseEmitter.event().name("sse"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // user의 고유 아이디값을 key값으로 해서 SseEmitter를 저장
        sseEmitters.put(principal.getUsername(), sseEmitter);

        sseEmitter.onCompletion(() -> sseEmitters.remove(principal.getUsername()));
        sseEmitter.onTimeout(() -> sseEmitters.remove(principal.getUsername()));
        sseEmitter.onError((e) -> sseEmitters.remove(principal.getUsername()));

        return sseEmitter;
    }
}