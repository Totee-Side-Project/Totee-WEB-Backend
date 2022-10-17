package com.study.totee.api.dto.post;

import com.study.totee.api.dto.comment.CommentResponseDto;
import com.study.totee.api.model.Post;
import com.study.totee.type.PeriodType;
import com.study.totee.type.PositionType;
import com.study.totee.utils.PositionConverter;
import com.study.totee.utils.SkillConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Getter
@NoArgsConstructor
public class PostResponseDto  {

    @ApiModelProperty(example = "포스트번호")
    private Long postId;

    @ApiModelProperty(example = "제목")
    private String title;

    @ApiModelProperty(example = "내용")
    private String content;

    @ApiModelProperty(example = "작성자")
    private String nickname;

    @ApiModelProperty(example = "조회수")
    private int view;

    @ApiModelProperty(example = "좋아요 수")
    private int likeNum;

    @ApiModelProperty(example = "댓글 수")
    private int commentNum;

    @ApiModelProperty(example = "댓글리스트")
    private List<CommentResponseDto> commentDTOList;

    @ApiModelProperty(example = "포스트 이미지 URL")
    private String imageUrl;

    @ApiModelProperty(example = "작성날짜")
    private LocalDateTime createdAt;

    @ApiModelProperty(name = "미팅 방식 (온라인 or 오프라인)")
    private String onlineOrOffline;

    @ApiModelProperty(name = "예상 기간")
    private PeriodType period;

    @ApiModelProperty(example = "모집 상태 (Y or N)")
    private String status;

    @ApiModelProperty(example = "모집 대상 포지션 리스트 (ex Design, FrontEnd..)")
    private List<String> positionList;

    @ApiModelProperty(example = "기술 스택 리스트 (ex JavaScript, C, Java)")
    private List<String> skillList;

    @ApiModelProperty(example = "모집 인원 수")
    private int recruitNum;

    @ApiModelProperty(example = "연락 방법")
    private String contactMethod;

    @ApiModelProperty(example = "연락 링크")
    private String contactLink;

    @ApiModelProperty(example = "지역")
    private String region;

    @ApiModelProperty(example = "상세주소")
    private String detailedRegion;

    @ApiModelProperty(example = "카테고리이름")
    private String categoryName;

    @ApiModelProperty(example = "작성자 포지션")
    private PositionType authorPosition;

    public PostResponseDto(Post post) {

        final PositionConverter positionConverter = new PositionConverter();
        final SkillConverter skillConverter = new SkillConverter();

        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.nickname = post.getUser().getUserInfo().getNickname();
        this.view = post.getView();
        this.likeNum = post.getLikeNum();
        this.commentNum = post.getCommentNum();
        this.imageUrl = post.getUser().getUserInfo().getProfileImageUrl();
        this.createdAt = post.getCreatedAt();
        this.onlineOrOffline = post.getOnlineOrOffline();
        this.period = post.getPeriod();
        this.status = post.getStatus();
        this.positionList = positionConverter.convertPositionEntityToString(post.getPositionList());
        this.recruitNum = post.getRecruitNum();
        this.contactMethod = post.getContactMethod();
        this.contactLink = post.getContactLink();
        this.skillList = skillConverter.convertSkillEntityToString(post.getSkillList());
        this.region = post.getRegion();
        this.detailedRegion = post.getDetailedRegion();
        this.categoryName = "스터디";
        this.authorPosition = post.getUser().getUserInfo().getPosition();
    }

    public PostResponseDto(Post post, List<CommentResponseDto> CommentResponseDtoList) {

        final PositionConverter positionConverter = new PositionConverter();
        final SkillConverter skillConverter = new SkillConverter();

        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.nickname = post.getUser().getUserInfo().getNickname();
        this.view = post.getView();
        this.likeNum = post.getLikeNum();
        this.commentNum = post.getCommentNum();
        this.imageUrl = post.getUser().getUserInfo().getProfileImageUrl();
        this.createdAt = post.getCreatedAt();
        this.onlineOrOffline = post.getOnlineOrOffline();
        this.period = post.getPeriod();
        this.status = post.getStatus();
        this.positionList = positionConverter.convertPositionEntityToString(post.getPositionList());
        this.recruitNum = post.getRecruitNum();
        this.contactMethod = post.getContactMethod();
        this.contactLink = post.getContactLink();
        this.commentDTOList = CommentResponseDtoList;
        this.skillList = skillConverter.convertSkillEntityToString(post.getSkillList());
        this.region = post.getRegion();
        this.detailedRegion = post.getDetailedRegion();
        this.categoryName = "스터디";
        this.authorPosition = post.getUser().getUserInfo().getPosition();
    }
}
