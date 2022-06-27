package com.study.totee.api.service;

import com.study.totee.api.dto.user.RoleRequestDto;
import com.study.totee.api.model.UserEntity;
import com.study.totee.api.model.UserInfoEntity;
import com.study.totee.api.persistence.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserInfoRepository userInfoRepository;

    // 유저의 권한을 변경하는 로직입니다.
    @Transactional
    public void updateRole(RoleRequestDto roleRequestDto){
        UserInfoEntity userinfo = userInfoRepository.findByNickname(roleRequestDto.getNickname()).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        validate(userinfo.getUser());
        userinfo.getUser().setRoleType(roleRequestDto.getRoleType());
    }

    private void validate(final UserEntity user){
        if(user == null){
            log.warn("Domain cannot be null.");
            throw new RuntimeException("Domain cannot be null");
        }

        if(user.getUsername() == null){
            log.warn("없는 사용자 입니다.");
            throw new RuntimeException("없는 사용자 입니다.");
        }
    }
}
