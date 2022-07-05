package com.study.totee.api.service;

import com.study.totee.api.dto.user.UserInfoRequestDto;
import com.study.totee.api.model.UserEntity;
import com.study.totee.api.model.UserInfoEntity;
import com.study.totee.api.persistence.UserInfoRepository;
import com.study.totee.api.persistence.UserRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;


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
        UserEntity user = userRepository.findById(userId);
        if (user == null) {
            throw new BadRequestException(ErrorCode.NO_USER_ERROR);
        }
        UserInfoEntity userInfoEntity = user.getUserInfo();
        // 유저 상세 정보 dto 에 프로필 이미지가 없을 경우 디폴트 이미지로 설정
        if (userInfoRequestDto.getProfileImage() == null) {
            user.setProfileImageUrl("https://lh3.googleusercontent.com/a-/AOh14Gg_jYj1ka2KSZcYgcxXxasvl8_rytXHtszA-SzRwg=s96-c");
        } // 유저 상세 정보 dto 에 프로필 이미지가 있을 경우 서버에 이미지 업로드
        else {
            user.setProfileImageUrl(awsS3Service.upload(userInfoRequestDto.getProfileImage(), "static"));
        }

        // 유저 상세 정보 dto 에서 Position Type 에 존재하지 않는 Position 을 입력하면 예외를 던짐
        try {
            userInfoEntity.setPosition(userInfoRequestDto.getPosition());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(ErrorCode.NO_POSITION_TYPE_ERROR);
        }
        // 닉네임 체크 한번 더
        if(userInfoRequestDto.getNickname().length() < 2 || userInfoRequestDto.getNickname().length() > 5){
            throw new BadRequestException(ErrorCode.INVALID_INPUT_ERROR);
        }
        if (isNicknameDuplicate(userInfoRequestDto.getNickname())) {
            throw new BadRequestException(ErrorCode.ALREADY_EXIST_NICKNAME_ERROR);
        }

        userInfoEntity.setNickname(userInfoRequestDto.getNickname());
        user.setUserInfo(userInfoEntity);
    }
    // 로컬 회원가입 테스트 용 삭제 예정
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

    // 닉네임 중복 체크
    public boolean isNicknameDuplicate(String nickname) {
        return userInfoRepository.existsByNickname(nickname);
    }
    // 아이디로 유저 조회
    public UserEntity getUser(final String id){
        return userRepository.findById(id);
    }
}