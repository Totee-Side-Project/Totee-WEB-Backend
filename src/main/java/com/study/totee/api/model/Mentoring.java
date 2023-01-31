package com.study.totee.api.model;

import com.study.totee.api.dto.mentoring.MentoringRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TB_MENTORING")
public class Mentoring {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENTORING_ID")
    private Long id;

    @Column(name = "COST")
    @NotNull
    private int cost;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    @Lob
    private String content;

    @Column
    private int score;

    @Column(name = "MENTEE_NUM")
    private int menteeNum;

    @OneToMany(mappedBy = "post")
    private List<Team> teamList;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @OneToMany(mappedBy = "mentoring", cascade = CascadeType.ALL)
    private List<Like> like;

    @OneToMany(mappedBy = "mentoring")
    private List<MentoringApplicant> mentoringApplicants;

    @Column(name = "LIKE_NUM")
    @NotNull
    private int likeNum;

    @Column(name = "REVIEW_SCORE")
    private float reviewScore;

    @OneToMany(mappedBy = "mentoring", cascade = CascadeType.ALL)
    private List<Review> reviewList;

    public Mentoring(MentoringRequestDto mentoringRequestDto, User user) {
        this.cost = mentoringRequestDto.getCost();
        this.score = 0;
        this.title = mentoringRequestDto.getTitle();
        this.content = mentoringRequestDto.getContent();
        this.menteeNum = 0;
        this.likeNum = 0;
        this.reviewScore = 0f;
        this.user = user;
        this.user.getUserInfo().increaseMentoringNum();
    }

    public void update(MentoringRequestDto mentoringRequestDto){
        this.cost = mentoringRequestDto.getCost();
        this.title = mentoringRequestDto.getTitle();
        this.content = mentoringRequestDto.getContent();
    }

    public void increaseScore() {this.score += 1f;}
    public void decreaseMenteeNum() {this.menteeNum -= 1;}

    public void increaseLikeNum() {
        this.likeNum += 1;
    }

    public void decreaseLikeNum() {
        this.likeNum -= 1;
    }

    public void addReview(Review review){
        this.reviewList.add(review);
        this.reviewScore += review.getScore();
        this.score += review.getScore() - 2f;
    }
}
