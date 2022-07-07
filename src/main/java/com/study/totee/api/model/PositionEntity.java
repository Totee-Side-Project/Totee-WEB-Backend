package com.study.totee.api.model;

import com.study.totee.type.PositionType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "TB_POSITION")
public class PositionEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "POSITION_ID")
    private Long id;

    @Column(name = "POSITION")
    @Enumerated(value = EnumType.STRING)
    private PositionType position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_INFO_ID")
    private UserInfoEntity userInfo;

    public PositionEntity(PositionType position, UserInfoEntity userInfo) {
        this.position = position;
        this.userInfo = userInfo;
        userInfo.getPositionList().add(this);
    }

    public PositionEntity(PositionType position, PostEntity post) {
        this.position = position;
        this.post = post;
        post.getPositionList().add(this);
    }
}