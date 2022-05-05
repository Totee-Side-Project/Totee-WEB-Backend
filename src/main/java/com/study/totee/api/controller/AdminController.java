package com.study.totee.api.controller;

import com.study.totee.api.dto.RoleDTO;
import com.study.totee.api.service.AdminService;
import com.study.totee.common.ApiResponse;
import com.study.totee.oauth.entity.RoleType;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @ApiOperation(value = "권한변경", notes = "권한 등급을 변경함.")
    @PutMapping("/api/v1/admin/update/role")
    public ApiResponse updateRole(@RequestBody RoleDTO roleDTO){
        String id = roleDTO.getId();
        RoleType role = roleDTO.getRoleType();
        adminService.updateRole(id, role);
        return ApiResponse.success("message", "Success");
    }
}
