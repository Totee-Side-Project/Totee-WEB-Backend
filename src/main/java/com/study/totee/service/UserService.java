package com.study.totee.service;

import com.study.totee.model.UserEntity;
import com.study.totee.model.UserInfoEntity;
import com.study.totee.persistence.UserInfoRepository;
import com.study.totee.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

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

    public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
        final UserEntity originalUser = userRepository.findByEmail(email);

        // matches 메서드를 이용해 패스워드가 같은지 확인
        if(originalUser != null && encoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        }
        return null;
    }

    public Optional<UserEntity> getUserId(final String id){
        return userRepository.findById(id);
    }
    public String getUserName(final String id) {
        Optional<UserEntity> user = userRepository.findById(id);
        return user.get().getUsername();
    }
}
