package com.study.totee.api.service;

import com.study.totee.api.dto.user.RoleRequestDto;
import com.study.totee.api.model.User;
import com.study.totee.api.model.UserInfo;
import com.study.totee.api.persistence.UserInfoQueryRepository;
import com.study.totee.api.persistence.UserInfoRepository;
import com.study.totee.api.persistence.UserQueryRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import com.study.totee.type.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserQueryRepository userQueryRepository;

    // 유저의 권한을 변경하는 로직입니다.
    @Transactional
    public void updateRole(RoleRequestDto roleRequestDto){
        // 존재 하지 않는 유저이면 예외를 던짐.
        User user = Optional.ofNullable(userQueryRepository.findByUserInfo_Nickname(roleRequestDto.getNickname())).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        user.setRoleType(roleRequestDto.getRoleType());
    }
}
