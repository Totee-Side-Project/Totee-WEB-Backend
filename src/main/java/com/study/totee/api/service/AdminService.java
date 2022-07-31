package com.study.totee.api.service;

import com.study.totee.api.dto.user.RoleRequestDto;
import com.study.totee.api.model.UserInfoEntity;
import com.study.totee.api.persistence.UserInfoRepository;
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

    private final UserInfoRepository userInfoRepository;

    // 유저의 권한을 변경하는 로직입니다.
    @Transactional
    public void updateRole(RoleRequestDto roleRequestDto){
        // 존재 하지 않는 유저이면 예외를 던짐.
        UserInfoEntity userinfo = Optional.ofNullable(userInfoRepository.findByNickname(roleRequestDto.getNickname())).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_USER_ERROR));
        // 맞지 않는 타입 예외
        if(roleRequestDto.getRoleType() != RoleType.ADMIN && roleRequestDto.getRoleType() != RoleType.USER){
            throw new BadRequestException(ErrorCode.INVALID_ROLE_TYPE_ERROR);
        }

        userinfo.getUser().setRoleType(roleRequestDto.getRoleType());
    }
}
