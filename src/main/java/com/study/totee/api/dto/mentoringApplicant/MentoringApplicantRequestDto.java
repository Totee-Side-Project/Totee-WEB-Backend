package com.study.totee.api.dto.mentoringApplicant;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MentoringApplicantRequestDto {
    @ApiModelProperty(example = "월, 화, 수, 금")
    private String week;

    @ApiModelProperty(example = "12:00")
    private String startTime;

    @ApiModelProperty(example = "14:00")
    private String endTime;

    @ApiModelProperty(example = "rnjstmdals6@gmail.com")
    private String contact;

    @ApiModelProperty(example = "멘토링 지원합니다")
    private String comment;
}
