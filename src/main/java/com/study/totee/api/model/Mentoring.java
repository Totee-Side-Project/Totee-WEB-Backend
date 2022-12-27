package com.study.totee.api.model;

import com.study.totee.api.dto.mentor.MentorRequestDto;
import com.study.totee.api.dto.mentoring.MentoringRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    public Mentoring(MentoringRequestDto mentoringRequestDto, User user) {
        this.cost = mentoringRequestDto.getCost();
        this.title = mentoringRequestDto.getTitle();
        this.content = mentoringRequestDto.getContent();
        this.user = user;
    }
    
}
