package com.study.totee.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.study.totee.api.dto.reply.ReplyRequestDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "TB_REPLY")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPLY_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMMENT_ID", nullable = false)
    @JsonIgnore
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "CONTENT")
    @Lob
    private String content;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "MODIFIED_AT")
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    public Reply(User user, Comment comment, ReplyRequestDto replyRequestDto) {
        this.user = user;
        this.comment = comment;
        this.nickname = user.getUserInfo().getNickname();
        this.content = replyRequestDto.getContent();
    }
}
