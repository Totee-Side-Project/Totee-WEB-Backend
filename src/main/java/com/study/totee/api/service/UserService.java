package com.study.totee.api.service;

import com.study.totee.api.dto.user.UserInfoRequestDto;
import com.study.totee.api.dto.user.UserInfoResponseDto;
import com.study.totee.api.dto.user.UserInfoUpdateRequestDto;
import com.study.totee.api.model.User;
import com.study.totee.api.model.UserInfo;
import com.study.totee.api.persistence.UserInfoQueryRepository;
import com.study.totee.api.persistence.UserInfoRepository;
import com.study.totee.api.persistence.UserQueryRepository;
import com.study.totee.api.persistence.UserRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserInfoQueryRepository userInfoQueryRepository;

    private final AwsS3Service awsS3Service;

    // 유저 상세 정보 설정
    @Transactional
    public UserInfoResponseDto setUserInfo(String userId, UserInfoRequestDto userInfoRequestDto) throws IOException {
        // 유저의 아이디로 유저정보 조회
        UserInfo userInfo = Optional.ofNullable(userInfoQueryRepository.findByUserId(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        // 닉네임 중복 검사(프론트의 중복검사기능이 완벽구현될 시 삭제예정)
        if (userInfoQueryRepository.existsByNickname(userInfoRequestDto.getNickname())) {
            throw new BadRequestException(ErrorCode.ALREADY_EXIST_NICKNAME_ERROR);
        }

        // 프로필 이미지가 있을 경우 이미지 업로드, 없을 경우 null 처리
        String profileImageUrl = (userInfoRequestDto.getProfileImage() == null) ?
                null : awsS3Service.upload(userInfoRequestDto.getProfileImage(), "static");

        userInfo.setUserInfo(userInfoRequestDto, profileImageUrl);
        return new UserInfoResponseDto(userInfo);
    }

    // 유저 상세 정보 수정
    @Transactional
    public UserInfoResponseDto updateUserInfo(String userId, UserInfoUpdateRequestDto userInfoUpdateRequestDto) throws IOException {
        // 유저의 아이디로 유저정보 조회
        UserInfo userInfo = Optional.ofNullable(userInfoQueryRepository.findByUserId(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        // 닉네임 중복 검사(프론트의 중복검사기능이 완벽구현될 시 삭제예정)
        if (!userInfo.getNickname().equals(userInfoUpdateRequestDto.getNickname())
                && userInfoQueryRepository.existsByNickname(userInfoUpdateRequestDto.getNickname())) {
            throw new BadRequestException(ErrorCode.ALREADY_EXIST_NICKNAME_ERROR);
        }

        // 프로필 이미지 수정&삭제
        String profileImageUrl = null;

        if(!userInfoUpdateRequestDto.getKeepProfileImage().equals("Y")){
            if(userInfo.getProfileImageUrl() != null){
                awsS3Service.fileDelete(userInfo.getProfileImageUrl());
            }
            profileImageUrl = userInfoUpdateRequestDto.getProfileImage().isEmpty() ?
                    null : awsS3Service.upload(userInfoUpdateRequestDto.getProfileImage(), "static");
        } else{
            profileImageUrl = userInfo.getProfileImageUrl();
        }
        return new UserInfoResponseDto(userInfo);
    }

    // 닉네임 중복 검사
    @Transactional(readOnly = true)
    public boolean existsByNickname(final String nickname){
        return userInfoQueryRepository.existsByNickname(nickname);
    }

    // 닉네임으로 유저 조회
    @Transactional(readOnly = true)
    public User getUserByNickname(final String nickname){
        return Optional.ofNullable(userQueryRepository.findByUserInfo_Nickname(nickname)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));
    }

    // 유저 아이디로 유저 조회
    @Transactional(readOnly = true)
    public User getUser(final String id){
        return userQueryRepository.findById(id);
    }

    //user id로 정보 반환
    public User loadUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));
    }

    // 포스트맨 회원가입 (테스트 용 삭제 예정)
    @Transactional
    public void create(final User userEntity1, final User userEntity2) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUser(userEntity1);
        userInfo.setNickname("테스트3");
        userInfo.setUser(userEntity1);
        UserInfo savedUserInfo = userInfoRepository.save(userInfo);
        userEntity1.setUserInfo(savedUserInfo);

        UserInfo userInfo2 = new UserInfo();
        userInfo2.setUser(userEntity2);
        userInfo2.setNickname("테스트2");
        userInfo2.setUser(userEntity2);
        UserInfo savedUserInfo2 = userInfoRepository.save(userInfo2);
        userEntity2.setUserInfo(savedUserInfo2);

        userRepository.save(userEntity1);
        userRepository.save(userEntity2);
    }
}