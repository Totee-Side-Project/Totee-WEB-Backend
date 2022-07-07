package com.study.totee.api.controller;

import com.study.totee.api.dto.user.RoleRequestDto;
import com.study.totee.api.service.AdminService;
import com.study.totee.common.ApiResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @ApiOperation(value = "권한변경", notes = "유저의 권한 등급을 변경합니다.")
    @PutMapping("/api/v1/admin/update/role")
    public ApiResponse updateRole(@RequestBody RoleRequestDto roleRequestDto){
        adminService.updateRole(roleRequestDto);
        return ApiResponse.success("message", "유저의 권한을 성공적으로 변경했습니다.");
    }
}
