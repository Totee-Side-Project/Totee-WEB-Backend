package com.study.totee.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "COMMENT_ENTITY")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "POST_ID", nullable = false)
    @JsonIgnore
    private PostEntity post;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @Column(name = "USER_NAME")
    private String username;

    @Lob
    @ApiModelProperty(example = "내용")
    String content;

    @CreationTimestamp
    @ApiModelProperty(example = "2022-01-06 14:57:42.777000 ---Insert 시 자동 삽입 넣지말아요")
    private LocalDateTime created_at;
}
