package com.study.totee.api.controller;

import com.study.totee.api.dto.admin.MentorApprovalRequestDto;
import com.study.totee.api.dto.user.RoleRequestDto;
import com.study.totee.api.service.AdminService;
import com.study.totee.common.ApiResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @ApiOperation(value = "권한변경", notes = "유저의 권한 등급을 변경합니다.")
    @PutMapping("/api/v1/admin/update/role")
    public ApiResponse<Object> updateRole(@Valid @RequestBody RoleRequestDto requestDto){
        adminService.updateRole(requestDto);
        return ApiResponse.success("message", "유저의 권한을 성공적으로 변경했습니다.");
    }

    @ApiOperation(value = "멘토 지원 승인/거절")
    @PostMapping("/api/v1/admin/mentor")
    public ApiResponse<Object> acceptMentor(@RequestBody MentorApprovalRequestDto requestDto){
        if (adminService.approvalMentor(requestDto)){
            return ApiResponse.success("message" , "성공적으로 멘토 지원 승인하였습니다.");
        }
        return ApiResponse.success("message" , "성공적으로 멘토 지원 거절 하였습니다.");
    }
}
