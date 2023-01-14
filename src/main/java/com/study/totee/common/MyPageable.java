package com.study.totee.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class MyPageable {
    @ApiModelProperty(value = "페이지 번호, 0부터 시작")
    private Integer page;

    @ApiModelProperty(value = "페이지 크기, 1 ~ 100")
    private Integer size;

    @ApiModelProperty(value = "정렬, 사용 예시 : 컬럼명,DESC")
    private List<String> sort;
}
