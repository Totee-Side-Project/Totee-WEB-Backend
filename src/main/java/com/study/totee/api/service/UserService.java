package com.study.totee.api.service;

import com.study.totee.api.dto.user.UserInfoRequestDto;
import com.study.totee.api.model.UserEntity;
import com.study.totee.api.model.UserInfoEntity;
import com.study.totee.api.persistence.UserInfoRepository;
import com.study.totee.api.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    public UserEntity getUser(final String id){
        return userRepository.findById(id);
    }

    @Transactional
    public boolean validate(String nickname){
        return userInfoRepository.existsByNickname(nickname);
    }

    @Transactional
    public void createUserInfo(String userId, UserInfoRequestDto userInfoRequestDto){
        UserEntity user = userRepository.findById(userId);
        UserInfoEntity userInfoEntity = user.getUserInfo();
        if (userInfoRequestDto.getProfileImage() == null){
            user.setProfileImageUrl("https://lh3.googleusercontent.com/a-/AOh14Gg_jYj1ka2KSZcYgcxXxasvl8_rytXHtszA-SzRwg=s96-c");
        }
        userInfoEntity.setPosition(userInfoRequestDto.getPosition());
        userInfoEntity.setNickname(userInfoRequestDto.getNickname());
        user.setUserInfo(userInfoEntity);
    }

    public void create(final UserEntity userEntity, final UserInfoEntity userInfoEntity) {
        if(userEntity == null || userInfoEntity == null || userEntity.getEmail() == null ) {
            throw new RuntimeException("Invalid arguments");
        }
        final String email = userEntity.getEmail();
        if(userRepository.existsByEmail(email)) {
            log.warn("Email already exists {}", email);
            throw new RuntimeException("Email already exists");
        }

        userRepository.save(userEntity);
        userInfoRepository.save(userInfoEntity);
    }

}