package com.study.totee.api.dto.applicant;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class ApplicantRequestDto {
    @ApiModelProperty(example = "지원하기")
    private String message;
}
