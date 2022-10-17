package com.study.totee.api.dto.post;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
@Getter
@NoArgsConstructor
public class PostRequestDto {
    @ApiModelProperty(example = "제목")
    private String title;

    @ApiModelProperty(example = "내용")
    private String content;

    @ApiModelProperty(example = "미팅 방식 (온라인 or 오프라인)")
    private String onlineOrOffline;

    @ApiModelProperty(example = "예상 기간 (ex 1개월 미만 or 1~3개월 or 3~6개월 or 6개월 이상)")
    private String period;

//    @ApiModelProperty(example = "포스트 썸네일 이미지")
//    private MultipartFile postImage;

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
}
