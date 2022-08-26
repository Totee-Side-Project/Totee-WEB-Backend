package com.study.totee.api.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_NOTIFICATION")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTIFICATION_ID")
    private Long id;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "URL")
    private String url;

    @Column(name = "IS_READ")
    private String isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_SEQ")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private LocalDateTime created_at;

    public Notification(Post post, User user) {
        this.post = post;
        this.user = post.getUser();
        this.content = user.getUserInfo().getNickname() + " 님이 " + post.getTitle() + " 게시글에 댓글을 남기셨습니다.";
        this.isRead = "N";
    }

    public Notification(Comment comment, User user) {
        this.post = comment.getPost();
        this.user = comment.getPost().getUser();
        this.content = user.getUserInfo().getNickname() + " 님이 " + comment.getContent() + " 댓글에 답글을 남기셨습니다.";
        this.isRead = "N";
    }

    public Notification(Like like, User user){
        this.post = like.getPost();
        this.user = like.getPost().getUser();
        this.content = user.getUserInfo().getNickname() + " 님이 " + like.getPost().getTitle() + " 게시글에 좋아요를 눌렀습니다.";
        this.isRead = "N";
    }
}