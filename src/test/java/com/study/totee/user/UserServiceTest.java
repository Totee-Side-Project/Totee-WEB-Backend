package com.study.totee.user;

import com.study.totee.api.dto.user.UserInfoRequestDto;
import com.study.totee.api.dto.user.UserInfoResponseDto;
import com.study.totee.api.dto.user.UserInfoUpdateRequestDto;
import com.study.totee.api.model.UserInfo;
import com.study.totee.api.persistence.UserInfoQueryRepository;
import com.study.totee.api.persistence.UserInfoRepository;
import com.study.totee.api.persistence.UserQueryRepository;
import com.study.totee.api.persistence.UserRepository;
import com.study.totee.api.service.UserService;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import com.study.totee.type.PositionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.study.totee.util.EntityFactory.userInfoRequestDto;
import static com.study.totee.util.EntityFactory.userInfoUpdateRequestDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService target;

    @Mock
    private UserInfoQueryRepository userInfoQueryRepository;

    @Test
    public void 유저정보설정성공() throws IOException {
        // given
        UserInfoRequestDto userInfoRequestDto = userInfoRequestDto(1);
        given(userInfoQueryRepository.findByUserId(any(String.class))).willReturn(new UserInfo());
        given(userInfoQueryRepository.existsByNickname(any(String.class))).willReturn(false);

        // when
        UserInfoResponseDto result = target.setUserInfo("TEST", userInfoRequestDto);

        // then
        assertThat(result.getNickname()).isEqualTo("user1");
        assertThat(result.getPosition()).isEqualTo(PositionType.FRONT_END);
        assertThat(result.getIntro()).isEqualTo("");
        assertThat(result.getProfileImageUrl()).isEqualTo(null);
        assertThat(result.getBackgroundImageUrl()).isEqualTo(null);
    }

    @Test
    public void 유저정보설정실패_유저아이디없음() throws IOException {
        // given
        UserInfoRequestDto userInfoRequestDto = userInfoRequestDto(1);
        given(userInfoQueryRepository.findByUserId(any(String.class))).willReturn(null);

        // when
        final BadRequestException result = assertThrows(BadRequestException.class, () -> target.setUserInfo("TEST", userInfoRequestDto));

        // then
        assertThat(result.getMessage()).isEqualTo(ErrorCode.NOT_EXIST_USER_ERROR.getMessage());
    }

    @Test
    public void 유저정보설정실패_중복닉네임() throws IOException {
        // given
        UserInfoRequestDto userInfoRequestDto = userInfoRequestDto(1);
        given(userInfoQueryRepository.findByUserId(any(String.class))).willReturn(new UserInfo());
        given(userInfoQueryRepository.existsByNickname(any(String.class))).willReturn(true);

        // when
        final BadRequestException result = assertThrows(BadRequestException.class, () -> target.setUserInfo("TEST", userInfoRequestDto));

        // then
        assertThat(result.getMessage()).isEqualTo(ErrorCode.ALREADY_EXIST_NICKNAME_ERROR.getMessage());
    }

    @Test
    public void 유저정보수정실패_유저아이디없음() throws IOException {
        // given
        UserInfoUpdateRequestDto userInfoUpdateRequestDto = UserInfoUpdateRequestDto
                .builder()
                .nickname("user1")
                .intro("")
                .position(PositionType.FRONT_END)
                .profileImage(null)
                .backgroundImage(null)
                .build();

        given(userInfoQueryRepository.findByUserId(any(String.class))).willReturn(null);

        // when
        final BadRequestException result = assertThrows(BadRequestException.class, () -> target.updateUserInfo("TEST", userInfoUpdateRequestDto));

        // then
        assertThat(result.getMessage()).isEqualTo(ErrorCode.NOT_EXIST_USER_ERROR.getMessage());
    }

    @Test
    public void 유저정보수정실패_닉네임중복() throws IOException{
        // given
        UserInfoUpdateRequestDto userInfoUpdateRequestDto = UserInfoUpdateRequestDto
                .builder()
                .nickname("user1")
                .intro("")
                .position(PositionType.FRONT_END)
                .profileImage(null)
                .backgroundImage(null)
                .build();

        given(userInfoQueryRepository.findByUserId(any(String.class))).willReturn(new UserInfo());
        given(userInfoQueryRepository.existsByNickname(any(String.class))).willReturn(true);

        // when
        final BadRequestException result = assertThrows(BadRequestException.class, () -> target.updateUserInfo("TEST", userInfoUpdateRequestDto));

        // then
        assertThat(result.getMessage()).isEqualTo(ErrorCode.ALREADY_EXIST_NICKNAME_ERROR.getMessage());
    }

    @Test
    public void 유저정보수정성공() throws IOException {
        // given
        UserInfoUpdateRequestDto userInfoUpdateRequestDto = UserInfoUpdateRequestDto
                .builder()
                .nickname("user2")
                .intro("test")
                .position(PositionType.BACK_END)
                .profileImage(null)
                .backgroundImage(null)
                .build();

        given(userInfoQueryRepository.findByUserId(any(String.class))).willReturn(new UserInfo());
        given(userInfoQueryRepository.existsByNickname(any(String.class))).willReturn(false);

        // when
        UserInfoResponseDto result = target.updateUserInfo("TEST", userInfoUpdateRequestDto);

        // then
        assertThat(result.getNickname()).isEqualTo("user2");
        assertThat(result.getPosition()).isEqualTo(PositionType.BACK_END);
        assertThat(result.getIntro()).isEqualTo("test");
    }

    @Test
    public void 닉네임중복검사성공(){
        // given
        given(userInfoQueryRepository.existsByNickname(any(String.class))).willReturn(false);

        // when
        boolean result = target.existsByNickname("TEST");

        // then
        assertThat(result).isEqualTo(false);
    }

    @Test
    public void 닉네임중복검사실패_중복(){
        // given
        given(userInfoQueryRepository.existsByNickname(any(String.class))).willReturn(true);

        // when
        boolean result = target.existsByNickname("TEST");

        // then
        assertThat(result).isEqualTo(true);
    }
}
