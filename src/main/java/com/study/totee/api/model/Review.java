package com.study.totee.api.model;

import com.study.totee.api.dto.review.ReviewRequestDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_REVIEW")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_ID")
    private Long id;

    @Column(name = "SCORE")
    private Float score;

    @Column(name = "COMMENT")
    @Lob
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_SEQ")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENTORING_ID")
    private Mentoring mentoring;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "MODIFIED_AT")
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    public Review(User user, Mentoring mentoring, ReviewRequestDto dto){
        this.comment = dto.getComment();
        this.score = dto.getScore();
        this.user = user;
        this.mentoring = mentoring;
    }
}
