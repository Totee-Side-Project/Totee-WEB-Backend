package com.study.totee.api.dto;

import com.study.totee.oauth.entity.RoleType;
import lombok.Data;

@Data
public class RoleDTO {
    private String id;
    private RoleType roleType;
}
