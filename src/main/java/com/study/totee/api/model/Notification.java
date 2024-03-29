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

    @Column(name = "LIKE_ID")
    private Long likeId;

    @Column(name = "COMMENT_ID")
    private Long commentId;

    @Column(name = "REPLY_ID")
    private Long replyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_SEQ")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENTORING_ID")
    private Mentoring mentoring;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private LocalDateTime created_at;

    public Notification(Post post, User user, Comment comment) {
        this.post = post;
        this.user = post.getUser();
        this.content = user.getUserInfo().getNickname() + " 님이 " + post.getTitle() + " 게시글에 댓글을 남기셨습니다.";
        this.isRead = "N";
        this.commentId = comment.getId();
    }

    public Notification(Comment comment, User user, Reply reply) {
        this.post = comment.getPost();
        this.user = comment.getUser();
        this.content = user.getUserInfo().getNickname() + " 님이 " + comment.getContent() + " 댓글에 답글을 남기셨습니다.";
        this.isRead = "N";
        this.replyId = reply.getId();
    }

    public Notification(Like like, User user){
        this.post = like.getPost();
        this.user = like.getPost().getUser();
        this.content = user.getUserInfo().getNickname() + " 님이 " + like.getPost().getTitle() + " 게시글에 좋아요를 눌렀습니다.";
        this.isRead = "N";
        this.likeId = like.getId();
    }

    // 스터디 작성자에게 알림
    public Notification(Applicant applicant, User user){
        this.post = applicant.getPost();
        this.user = applicant.getPost().getUser();
        this.content = user.getUserInfo().getNickname() + " 님이 " + applicant.getPost().getTitle() + "스터디에 지원하였습니다.";
        this.isRead = "N";
    }

    // 스터디 지원자에게 알림
    public Notification(Applicant applicant, boolean accept){
        this.post = applicant.getPost();
        this.user = applicant.getUser();
        this.content = accept ? applicant.getPost().getTitle() + "스터디에서 지원이 승인되었습니다." :
                applicant.getPost().getTitle() + "스터디에서 지원이 거부되었습니다.";
        this.isRead = "N";
    }

    // 멘토 지원자에게 알림
    public Notification(User user, boolean accept){
        this.user = user;
        this.content = accept ? "축하합니다! 멘토 지원이 승인되었습니다." : "멘토 지원이 거부되었습니다. 관리자에게 문의하세요.";
        this.isRead = "N";
    }
}
