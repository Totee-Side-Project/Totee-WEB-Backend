package com.study.totee.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
@Table(name = "POST_ENTITY")
public class PostEntity {
    @Id
    @Column(name = "POST_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column
    @ApiModelProperty(example = "제목")
    String title;

    @Column
    @ApiModelProperty(example = "썸네일 소개")
    String intro;

    @Lob
    @ApiModelProperty(example = "내용")
    String content;

    @Column
    @ApiModelProperty(example = "모집마감여부 0")
    @NotNull
    private boolean status;

    @CreationTimestamp
    @ApiModelProperty
    private LocalDateTime created_at;

    @ManyToOne(optional = false , cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID" , nullable = false)
    private UserEntity user;
}
