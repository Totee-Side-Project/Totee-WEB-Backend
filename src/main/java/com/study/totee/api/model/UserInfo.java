package com.study.totee.api.model;

import com.study.totee.api.dto.user.UserInfoRequestDto;
import com.study.totee.api.dto.user.UserInfoUpdateRequestDto;
import com.study.totee.type.PositionType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_USER_INFO")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_INFO_ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Size(min = 2, max = 5)
    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "INTRO", length = 500)
    private String intro;

    @Column(name = "PROFILE_IMAGE_URL", length = 512)
    private String profileImageUrl;

    @Column(name = "BACKGROUND_IMAGE_URL", length = 512)
    private String backgroundImageUrl;

    @Column(name = "POSITION", length = 20)
    @Enumerated(EnumType.STRING)
    private PositionType position;

    @OneToMany(mappedBy = "userInfo")
    private Set<Position> positionList;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "MODIFIED_AT")
    private LocalDateTime modifiedAt;

    @Column(name = "STUDY_NUM")
    private int studyNum;

    @Column(name = "MENTORING_NUM")
    private int mentoringNum;

    public void increaseStudyNum(){
        this.studyNum += 1;
    }

    public void decreaseStudyNum(){
        this.studyNum -= 1;
    }

    public void increaseMentoringNum(){
        this.mentoringNum += 1;
    }

    public void decreaseMentoringNum(){
        this.mentoringNum -= 1;
    }
    public void setUserInfo(UserInfoRequestDto userInfoRequestDto, String profileImageUrl) {
        this.nickname = userInfoRequestDto.getNickname();
        this.profileImageUrl = profileImageUrl;
        this.position = userInfoRequestDto.getPosition();
        this.intro = "";
        this.backgroundImageUrl = null;
        this.studyNum = 0;
        this.mentoringNum = 0;
    }

    public void updateUserInfo(UserInfoUpdateRequestDto userInfoUpdateRequestDto,
                               String profileImageUrl, String backgroundImageUrl) {
        this.nickname = userInfoUpdateRequestDto.getNickname();
        this.position = userInfoUpdateRequestDto.getPosition();
        this.intro = userInfoUpdateRequestDto.getIntro();
        this.profileImageUrl = profileImageUrl;
        this.backgroundImageUrl = backgroundImageUrl;
    }
}
