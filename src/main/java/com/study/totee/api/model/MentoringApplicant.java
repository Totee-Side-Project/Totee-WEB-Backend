package com.study.totee.api.model;

import com.study.totee.api.dto.mentoringApplicant.MentoringApplicantRequestDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "TB_MENTORING_APPLICANT")
public class MentoringApplicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENTORING_APPLICANT_ID")
    private Long id;

    @Column(name = "COMMENT")
    @Lob
    private String comment;

    @Column
    private String startTime;

    @Column
    private String endTime;

    @Column
    private String contact;

    @Column
    private String week;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_SEQ")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MENTORING_ID")
    private Mentoring mentoring;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    private String checking;

    public MentoringApplicant(User user, Mentoring mentoring, MentoringApplicantRequestDto dto){
        this.mentoring = mentoring;
        this.user = user;
        this.comment = dto.getComment();
        this.contact = dto.getContact();
        this.startTime = dto.getStartTime();
        this.endTime = dto.getEndTime();
        this.week = dto.getWeek();
        this.checking = "false";
        mentoring.getMentoringApplicants().add(this);
        user.getMentoringApplicantList().add(this);
    }

    public void deleteApply() {
        mentoring.getMentoringApplicants().remove(this);
        user.getMentoringApplicantList().remove(this);
    }

    public void setChecking(){
        this.checking = "true";
    }
}
