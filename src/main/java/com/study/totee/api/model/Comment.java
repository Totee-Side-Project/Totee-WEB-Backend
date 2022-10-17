package com.study.totee.api.model;

import com.study.totee.api.dto.comment.CommentRequestDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "TB_COMMENT")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;

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

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<Reply> reply;

    public Comment(User user, Post post, CommentRequestDto commentRequestDto) {
        this.post = post;
        this.user = user;
        this.nickname = user.getUserInfo().getNickname();
        this.content = commentRequestDto.getContent();
    }

    public void addReply(Reply reply) {
        this.reply.add(reply);
        this.post.increaseCommentNum();
    }
}
