package com.study.totee.api.service;

import com.study.totee.api.dto.user.UserInfoRequestDto;
import com.study.totee.api.dto.user.UserInfoUpdateRequestDto;
import com.study.totee.api.model.User;
import com.study.totee.api.model.UserInfo;
import com.study.totee.api.persistence.UserInfoRepository;
import com.study.totee.api.persistence.UserRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

import static com.study.totee.api.model.QUser.user;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final AwsS3Service awsS3Service;

    // 유저 상세 정보 추가
    @Transactional
    public void createUserInfo(String userId, UserInfoRequestDto userInfoRequestDto) throws IOException {
        UserInfo userInfo = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_USER_ERROR)).getUserInfo();

        // 닉네임 존재 검사
        if (isNicknameDuplicate(userInfoRequestDto.getNickname())) {
            throw new BadRequestException(ErrorCode.ALREADY_EXIST_NICKNAME_ERROR);
        }

        if(!userInfoRequestDto.getProfileImage().isEmpty()){
            userInfo.setProfileImageUrl(awsS3Service.upload(userInfoRequestDto.getProfileImage(), "static"));
        } else {
            userInfo.setProfileImageUrl(null);
        }
        userInfo.setPosition(userInfoRequestDto.getPosition());
        userInfo.setNickname(userInfoRequestDto.getNickname());
    }

    // 유저 상세 정보 수정
    @Transactional
    public void updateUserInfo(String userId, UserInfoUpdateRequestDto userInfoUpdateRequestDto) throws IOException {
        UserInfo userInfo = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_USER_ERROR)).getUserInfo();

        // 닉네임 체크
        if (!userInfo.getNickname().equals(userInfoUpdateRequestDto.getNickname())
                && isNicknameDuplicate(userInfoUpdateRequestDto.getNickname())) {
            throw new BadRequestException(ErrorCode.ALREADY_EXIST_NICKNAME_ERROR);
        }

        // 이미지 체크(기존 이미지를 삭제)
        if(userInfo.getProfileImageUrl() != null){
            awsS3Service.fileDelete(userInfo.getProfileImageUrl());
        }
        if (userInfo.getBackgroundImageUrl() != null) {
            awsS3Service.fileDelete(userInfo.getBackgroundImageUrl());
        }

        // dto 의 이미지를 업로드
        if (userInfoUpdateRequestDto.getProfileImage().isEmpty()) {
            userInfo.setProfileImageUrl(null);
        } else {
            userInfo.setProfileImageUrl(awsS3Service.upload(userInfoUpdateRequestDto.getProfileImage(), "static"));
        }
        if (userInfoUpdateRequestDto.getBackgroundImage().isEmpty()) {
            userInfo.setBackgroundImageUrl(null);
        } else {
            userInfo.setBackgroundImageUrl(awsS3Service.upload(userInfoUpdateRequestDto.getBackgroundImage(), "static"));
        }

        userInfo.setPosition(userInfoUpdateRequestDto.getPosition());
        userInfo.setNickname(userInfoUpdateRequestDto.getNickname());
        userInfo.setIntro(userInfoUpdateRequestDto.getIntro());
    }

    // 로컬 회원가입 테스트 용 삭제 예정
    public void create(final User userEntity) {

        userRepository.saveAndFlush(userEntity);
    }

    // 닉네임 중복 체크
    public boolean isNicknameDuplicate(String nickname) {
        return userInfoRepository.existsByNickname(nickname);
    }
    // 아이디로 유저 조회
    public User getUser(final String id){
        return userRepository.findById(id);
    }
    // 닉네임으로 유저 조회
    public User getUserByNickname(final String nickname){
        return userRepository.findByUserInfo_Nickname(nickname);
    }
}