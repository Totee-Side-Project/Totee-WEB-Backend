//package com.study.totee.user;
//
//import com.study.totee.api.dto.user.UserInfoRequestDto;
//import com.study.totee.api.model.User;
//import com.study.totee.api.model.UserInfo;
//import com.study.totee.api.persistence.UserInfoRepository;
//import com.study.totee.api.persistence.UserQueryRepository;
//import com.study.totee.api.persistence.UserRepository;
//import com.study.totee.type.ProviderType;
//import com.study.totee.type.RoleType;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
//import static com.study.totee.util.EntityFactory.userInfoRequestDto;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@Transactional
//@SpringBootTest
//public class UserQueryRepositoryTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UserInfoRepository userInfoRepository;
//
//    @Autowired
//    private UserQueryRepository userQueryRepository;
//
//    @Test
//    public void 유저_아이디로조회() {
//        // given
//        String id = "user1";
//
//        User user = new User(
//                "user1",
//                "user1",
//                "user1",
//                "Y",
//                "user1",
//                ProviderType.NAVER,
//                RoleType.USER,
//                LocalDateTime.now(),
//                LocalDateTime.now(),
//                new UserInfo()
//        );
//
//        // when
//        userRepository.save(user);
//        User result = userQueryRepository.findById(id);
//
//        //then
//        assertThat(result.getId()).isEqualTo(id);
//    }
//
//    @Test
//    public void 유저_닉네임으로조회(){
//        //given
//        String nickName = "user1";
//
//        UserInfo userInfo = new UserInfo();
//        userInfo.setUserInfo(userInfoRequestDto(1), null);
//        UserInfo savedUserInfo = userInfoRepository.save(userInfo);
//
//        User user = new User(
//                "user1",
//                "user1",
//                "user1",
//                "Y",
//                "user1",
//                ProviderType.NAVER,
//                RoleType.USER,
//                LocalDateTime.now(),
//                LocalDateTime.now(),
//                savedUserInfo
//        );
//        User savedUser = userRepository.save(user);
//        savedUserInfo.setUser(savedUser);
//
//        // when
//        User result = userQueryRepository.findByUserInfo_Nickname(nickName);
//
//        //then
//        assertThat(result.getId()).isEqualTo("user1");
//        assertThat(result.getUserInfo().getId()).isEqualTo(1L);
//    }
//}
