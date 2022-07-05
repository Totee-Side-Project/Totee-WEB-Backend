package com.study.totee.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.study.totee.type.PositionType;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "TB_USER_INFO")
public class UserInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_INFO_ID")
    private int userInfoId;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    private UserEntity user;

    @Column
    private String nickname;

    @Column
    private PositionType position;

    @OneToMany(mappedBy = "userInfo")
    private Set<PositionEntity> positionList;

}
