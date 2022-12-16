package com.study.totee.api.dto.mentor;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class MentorRequestDto {
	@ApiModelProperty(example = "분야")
	private String field;

	@ApiModelProperty(example = "경력")
	private String career;

	@ApiModelProperty(example = "개인 연락처")
	private String contact;

	@ApiModelProperty(example = "개인 포트폴리오")
	private String portfolioUrl;

	@ApiModelProperty(example = "자기소개 코멘트")
	private String comment;
}
