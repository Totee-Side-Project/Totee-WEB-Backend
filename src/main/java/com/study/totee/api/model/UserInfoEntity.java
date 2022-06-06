package com.study.totee.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "USER_INFO_ENTITY")
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
    private String major;

    @Column
    private String grade;

}
