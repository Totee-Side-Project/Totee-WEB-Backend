package com.study.totee.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.study.totee.type.PositionType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
    private Long userInfoId;

    @OneToOne
    @JoinColumn(name = "USER_SEQ")
    @JsonIgnore
    private UserEntity user;

    @Column(name = "NICKNAME", length = 5)
    private String nickname;

    @Column(name = "POSITION", length = 20)
    @Enumerated(EnumType.STRING)
    private PositionType position;

    @OneToMany(mappedBy = "userInfo")
    private Set<PositionEntity> positionList;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    @NotNull
    private LocalDateTime createdAt;

    @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "MODIFIED_AT")
    @NotNull
    private LocalDateTime modifiedAt;

}
