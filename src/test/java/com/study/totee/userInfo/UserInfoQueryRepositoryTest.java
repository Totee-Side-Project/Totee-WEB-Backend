package com.study.totee.userInfo;

import com.study.totee.api.model.User;
import com.study.totee.api.model.UserInfo;
import com.study.totee.api.persistence.UserInfoQueryRepository;
import com.study.totee.api.persistence.UserInfoRepository;
import com.study.totee.api.persistence.UserQueryRepository;
import com.study.totee.api.persistence.UserRepository;
import com.study.totee.type.ProviderType;
import com.study.totee.type.RoleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.study.totee.util.EntityFactory.userInfoRequestDto;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@SpringBootTest
public class UserInfoQueryRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserInfoQueryRepository userInfoQueryRepository;

    @Test
    public void 유저정보_유저아이디로조회(){
        //given
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfo(userInfoRequestDto(1), null);
        UserInfo savedUserInfo = userInfoRepository.save(userInfo);

        User user = new User(
                "user1",
                "user1",
                "user1",
                "Y",
                "user1",
                ProviderType.NAVER,
                RoleType.USER,
                LocalDateTime.now(),
                LocalDateTime.now(),
                savedUserInfo
        );
        User savedUser = userRepository.save(user);
        savedUserInfo.setUser(savedUser);

        //when
        UserInfo result = userInfoQueryRepository.findByUserId("user1");

        //then
        assertThat(result.getNickname()).isEqualTo("user1");
    }

    @Test
    public void 유저정보_닉네임중복체크(){
        //given
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfo(userInfoRequestDto(1), null);
        UserInfo savedUserInfo = userInfoRepository.save(userInfo);

        User user = new User(
                "user1",
                "user1",
                "user1",
                "Y",
                "user1",
                ProviderType.NAVER,
                RoleType.USER,
                LocalDateTime.now(),
                LocalDateTime.now(),
                savedUserInfo
        );
        User savedUser = userRepository.save(user);
        savedUserInfo.setUser(savedUser);
        //when
        boolean result = userInfoQueryRepository.existsByNickname("user1");
        //then
        assertThat(result).isTrue();
    }
}
