package com.study.totee.api.service;

import com.study.totee.api.dto.mentoring.MentoringRequestDto;
import com.study.totee.api.dto.mentoring.MentoringResponseDto;
import com.study.totee.api.dto.post.PostResponseDto;
import com.study.totee.api.model.*;
import com.study.totee.api.persistence.MentoringApplicantRepository;
import com.study.totee.api.persistence.MentoringRepository;
import com.study.totee.api.persistence.TeamRepository;
import com.study.totee.api.persistence.UserRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.ForbiddenException;
import com.study.totee.type.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentoringService {

    private final UserRepository userRepository;
    private final MentoringRepository mentoringRepository;
    private final MentoringApplicantRepository mentoringApplicantRepository;
    private final TeamRepository teamRepository;



    @Transactional
    public void save(String userId, MentoringRequestDto requestDto) throws IOException {
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        if(user.getRoleType().equals(RoleType.user)){
            throw new BadRequestException(ErrorCode.NO_AUTHORIZATION_ERROR);
        }

        Mentoring savedMentoring = mentoringRepository.save(new Mentoring(requestDto, user));
        teamRepository.save(new Team(user, savedMentoring));
    }

    @Transactional
    public void update(String userId, MentoringRequestDto mentoringRequestDto, Long mentoringId){
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        Mentoring mentoring = Optional.ofNullable(mentoringRepository.findByIdAndUser(mentoringId, user)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));

        mentoring.update(mentoringRequestDto);
    }

    @Transactional
    public void delete(Long mentoringId, String userId){
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        Mentoring mentoring = Optional.ofNullable(mentoringRepository.findByIdAndUser(mentoringId, user)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));

        user.getUserInfo().decreaseMentoringNum();
        mentoringApplicantRepository.deleteAllByMentoring(mentoring);
        teamRepository.deleteAllByMentoring(mentoring);
        mentoringRepository.delete(mentoring);
    }

    @Transactional(readOnly = true)
    public Mentoring findByMentoringId(Long id){
        return mentoringRepository.findById(id).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));
    }

    @Transactional(readOnly = true)
    public Page<MentoringResponseDto> findAll(Pageable pageable){
        return mentoringRepository.findAll(pageable).map(MentoringResponseDto::new);
    }

    @Transactional(readOnly = true)
    public Page<MentoringResponseDto> findAllByTitleContaining(Pageable pageable, String title){
        return mentoringRepository.findAllByTitleContaining(title, pageable).map(MentoringResponseDto::new);
    }

    @Transactional(readOnly = true)
    public Page<MentoringResponseDto> findAllByMyMentoringTeam(Pageable pageable, String userId){
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        return mentoringRepository.findAllByMyMentoringTeam(user, pageable).map(MentoringResponseDto::new);
    }

    @Transactional(readOnly = true)
    public Page<MentoringResponseDto> findAllByLikedPost(String id, Pageable pageable) {
        User user = Optional.ofNullable(userRepository.findById(id)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        return mentoringRepository.findAllByLikedMentoring(user, pageable).map(MentoringResponseDto::new);
    }

    @Transactional(readOnly = true)
    public Page<MentoringResponseDto> findAllByUser(String id, Pageable pageable) {
        User user = Optional.ofNullable(userRepository.findById(id)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        return mentoringRepository.findAllByUser(pageable, user).map(MentoringResponseDto::new);
    }

    @Transactional
    public Mentoring loadMentoringIfOwner(Long mentoringId, User user) {
        return Optional.ofNullable(mentoringRepository.findByIdAndUser(mentoringId, user)).orElseThrow(
                () -> new ForbiddenException(ErrorCode.NO_AUTHORIZATION_ERROR)
        );
    }
}
