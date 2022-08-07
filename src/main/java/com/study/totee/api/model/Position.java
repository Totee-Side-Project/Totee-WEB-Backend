package com.study.totee.api.model;

import com.study.totee.type.PositionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TB_POSITION")
public class Position {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "POSITION_ID")
    private Long id;

    @Column(name = "POSITION")
    @Enumerated(value = EnumType.STRING)
    private PositionType position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_INFO_ID")
    private UserInfo userInfo;

    public Position(PositionType position, UserInfo userInfo) {
        this.position = position;
        this.userInfo = userInfo;
        userInfo.getPositionList().add(this);
    }

    public Position(PositionType position, Post post) {
        this.position = position;
        this.post = post;
        post.getPositionList().add(this);
    }
}