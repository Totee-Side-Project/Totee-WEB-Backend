package com.study.totee.api.model;

import com.study.totee.api.dto.mentor.MentorRequestDto;
import com.study.totee.api.dto.user.UserInfoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TB_MENTOR")
public class Mentor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENTOR_ID")
    private Long id;

    @Column(name = "FIELD")
    private String field;

    @Column(name = "CAREER")
    private String career;

    @Column(name = "CONTACT")
    private String contact;

    @Column(name = "PORTFOLIO_URL")
    private String portfolioUrl;

    @Column(name = "COMMENT")
    @Lob
    private String comment;

    @Column(name = "APPROVAL")
    private String approval;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public Mentor(MentorRequestDto mentorRequestDto, User user) {
        this.field = mentorRequestDto.getField();
        this.career = mentorRequestDto.getCareer();
        this.contact = mentorRequestDto.getContact();
        this.portfolioUrl = mentorRequestDto.getPortfolioUrl();
        this.comment = mentorRequestDto.getComment();
        this.approval = "n";
        this.user = user;
    }

}
