package com.study.totee.api.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.study.totee.api.dto.post.PostRequestDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
@Table(name = "TB_POST")
public class PostEntity {
    @Id
    @Column(name = "POST_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    @Lob
    private String content;

    @Column(name = "STATUS", length = 1)
    @NotNull
    @Size(min = 1, max = 1)
    private String status;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    @ApiModelProperty
    private LocalDateTime created_at;

    @Column(name = "MODIFIED_AT")
    @LastModifiedDate
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime modifiedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @Column(name = "VIEW")
    @NotNull
    private int view;

    @OneToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private CategoryEntity category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<LikeEntity> like;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<CommentEntity> comment;

    @Column(name = "IMAGE_URL")
    @ApiModelProperty(example = "썸네일 이미지 URL")
    private String imageUrl;

    @Column(name = "ONLINE_OR_OFFLINE")
    private String onlineOrOffline;

    @Column(name = "PERIOD")
    private int period;

    @Column(name = "COMMENT_NUM")
    private int commentNum;

    @Column(name = "LIKE_NUM")
    private int likeNum;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<PositionEntity> positionList;

    @Column(name = "CONTACT_METHOD")
    private String contactMethod;

    @Column(name = "CONTACT_LINK")
    private String contactLink;

    public void updatePositionList(List<PositionEntity> positionList) {
        this.positionList = new HashSet<>(positionList);
    }

    @Column
    private int recruitNum;

    public void update(PostRequestDto postRequestDto, CategoryEntity category) {
        this.content = postRequestDto.getContent();
        this.title = postRequestDto.getTitle();
        this.status = postRequestDto.getStatus();
        this.onlineOrOffline = postRequestDto.getOnlineOrOffline();
        this.period = postRequestDto.getPeriod();
        this.category = category;
    }
}
