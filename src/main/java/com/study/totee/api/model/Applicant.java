package com.study.totee.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "TB_APPLICANT")
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPLICANT_ID")
    private Long id;

    @Column(name = "MESSAGE")
    @Lob
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_SEQ")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="POST_ID")
    private Post post;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    @ApiModelProperty
    private LocalDateTime createdAt;

    public Applicant(User user, Post post, String message){
        this.post = post;
        this.user = user;
        this.message = message;
        post.getApplicantList().add(this);
        user.getApplicantList().add(this);
    }

    public void deleteApply() {
        this.user.getApplicantList().remove(this);
        this.post.getApplicantList().remove(this);
    }
}
