package com.study.totee.api.model;


import com.study.totee.api.dto.post.PostRequestDto;
import com.study.totee.type.PeriodType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
@Table(name = "TB_POST")
public class Post {
    @Id
    @Column(name = "POST_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    @Lob
    private String content;

    @Column(name = "STATUS")
    @NotNull
    private String status;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    @ApiModelProperty
    private LocalDateTime createdAt;

    @Column(name = "MODIFIED_AT")
    @LastModifiedDate
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "VIEW")
    @NotNull
    private int view;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Like> like;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comment;

    @Column(name = "IMAGE_URL")
    @ApiModelProperty(example = "썸네일 이미지 URL")
    private String imageUrl;

    @Column(name = "ONLINE_OR_OFFLINE")
    private String onlineOrOffline;

    @Column(name = "PERIOD")
    private PeriodType period;

    @Column(name = "COMMENT_NUM")
    private int commentNum;

    @Column(name = "LIKE_NUM")
    private int likeNum;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Position> positionList;

    @Column(name = "CONTACT_METHOD")
    private String contactMethod;

    @Column(name = "CONTACT_LINK")
    private String contactLink;

    @Column(name ="RECRUIT_NUM")
    private String recruitNum;

    public Post(User user, Category category, PostRequestDto postRequestDto){
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.status = "Y";
        this.view = 0;
        this.category = category;
        this.onlineOrOffline = postRequestDto.getOnlineOrOffline();
        this.period = PeriodType.of(postRequestDto.getPeriod());
        this.contactMethod = postRequestDto.getContactMethod();
        this.contactLink = postRequestDto.getContactLink();
        this.recruitNum = postRequestDto.getRecruitNum();
        this.user = user;
        this.positionList = new HashSet<>();
    }

    public void update(PostRequestDto postRequestDto, Category category, List<Position> positionList) {
        this.content = postRequestDto.getContent();
        this.title = postRequestDto.getTitle();
        this.onlineOrOffline = postRequestDto.getOnlineOrOffline();
        this.period = PeriodType.of(postRequestDto.getPeriod());
        this.recruitNum = postRequestDto.getRecruitNum();
        this.category = category;
        this.positionList = new HashSet<>(positionList);
    }

    public void addComment(Comment comment) {
        this.commentNum += 1;
        this.comment.add(comment);
    }

    public void increaseView(){
        this.view += 1;
    }

    public void increaseLikeNum(){
        this.likeNum += 1;
    }

    public void decreaseLikeNum(){
        this.likeNum -= 1;
    }

    public void increaseCommentNum(){
        this.commentNum += 1;
    }

    public void decreaseCommentNum(Comment comment){
        this.commentNum -= (comment.getReply().size() + 1);
    }

    public void decreaseCommentNum(){
        this.commentNum -= 1;
    }

    public void updateStatus(String status) {
        this.status = status.equals("Y") ? "N" : "Y";
    }

}
