package com.study.totee.api.dto.admin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class MentorApprovalRequestDto {
    @ApiModelProperty(example = "유저 닉네임")
    private String nickname;

    @ApiModelProperty(example = "승인 / 거절")
    private boolean accept;
}
