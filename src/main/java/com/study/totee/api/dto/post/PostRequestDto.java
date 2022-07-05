package com.study.totee.api.dto.post;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {
    @ApiModelProperty(example = "제목")
    private String title;

    @ApiModelProperty(example = "내용")
    private String content;

    @ApiModelProperty(example = "카테고리")
    private String categoryName;

    @ApiModelProperty(example = "모집 상태 (Y or N)")
    private String status;

    @ApiModelProperty(example = "미팅 방식 (ON or OFF)")
    private String onlineOrOffline;

    @ApiModelProperty(example = "예상 기간 (월 단위)")
    private int period;

    @ApiModelProperty(example = "포스트 썸네일 이미지")
    private MultipartFile postImage;

    @ApiModelProperty(example = "모집 대상 포지션 리스트 (ex Designer, Front_End..)")
    private List<String> positionList;

    @ApiModelProperty(example = "모집 인원 수")
    private int recruitNum;
}
