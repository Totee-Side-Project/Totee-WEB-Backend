package com.study.totee.api.controller;

import com.study.totee.api.service.NotificationService;
import com.study.totee.common.ApiResponse;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.NoAuthException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @ApiOperation(value = "알림 목록 조회", notes = "알림 목록을 조회합니다.")
    @GetMapping("/api/v1/notification")
    public ApiResponse<Object> getNotificationList(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        String id = Optional.ofNullable(principal).orElseThrow(() -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();

        return ApiResponse.success("data", notificationService.notificationDtoList(id));
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
}
