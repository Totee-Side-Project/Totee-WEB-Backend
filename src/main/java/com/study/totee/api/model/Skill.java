package com.study.totee.api.model;

import com.study.totee.type.PositionType;
import com.study.totee.type.SkillType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TB_SKILL")
public class Skill {

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "SKILL_ID")
    private Long id;

    @Column(name = "SKILL")
    @Enumerated(value = EnumType.STRING)
    private SkillType skill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;


    public Skill(SkillType skill, Post post) {
        this.skill = skill;
        this.post = post;
        post.getSkillList().add(this);
    }
}