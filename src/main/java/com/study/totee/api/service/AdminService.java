package com.study.totee.api.service;

import com.study.totee.api.model.UserEntity;
import com.study.totee.api.persistence.UserRepository;
import com.study.totee.oauth.entity.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    @Transactional
    public void updateRole(String id, RoleType roleType){
        UserEntity user = userRepository.findById(id);
        validate(user);
        user.setRoleType(roleType);
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
