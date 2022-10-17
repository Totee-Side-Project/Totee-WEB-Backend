package com.study.totee.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "TB_TEAM")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_SEQ")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="POST_ID")
    private Post post;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    @ApiModelProperty
    private LocalDateTime createdAt;

    public Team(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public void deleteTeam() {
        this.post.decreaseMemberNum();
        this.post.getTeamList().remove(this);
        this.user.getTeamList().remove(this);
    }
}
