package com.study.totee.api.dto.notification;


import com.study.totee.api.dto.License;
import com.study.totee.api.model.Notification;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Optional;


@Getter
@NoArgsConstructor
public class NotificationResponseDto extends RepresentationModel<NotificationResponseDto> {

    @ApiModelProperty(value = "알림 아이디")
    private Long notificationId;

    @ApiModelProperty(example = "알림 내용")
    private String content;

    @ApiModelProperty(example = "알림 읽음여부")
    private String isRead;

    @ApiModelProperty(example = "링크 이동을 위한 포스트번호")
    private Long postId;

    @ApiModelProperty(example = "알림 생성 날짜")
    private LocalDateTime createdAt;

    public NotificationResponseDto(Notification notification) {
        this.notificationId = notification.getId();
        this.content = notification.getContent();
        this.isRead = notification.getIsRead();
        this.createdAt = notification.getCreated_at();
        this.postId = notification.getPost() == null ? null : notification.getPost().getId();
    }
}
