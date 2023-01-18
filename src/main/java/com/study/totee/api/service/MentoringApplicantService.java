package com.study.totee.api.service;

import com.study.totee.api.dto.mentoringApplicant.MentoringApplicantRequestDto;
import com.study.totee.api.dto.team.MemberListResponseDto;
import com.study.totee.api.dto.team.MenteeListResponseDto;
import com.study.totee.api.model.Mentoring;
import com.study.totee.api.model.MentoringApplicant;
import com.study.totee.api.model.Post;
import com.study.totee.api.model.User;
import com.study.totee.api.persistence.*;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentoringApplicantService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final TeamQueryRepository teamQueryRepository;
    private final MentoringRepository mentoringRepository;
    private final MentoringApplicantRepository mentoringApplicantRepository;

    @Transactional
    public void applyMentoring(String userId, Long mentoringId, MentoringApplicantRequestDto dto){
        // [예외처리] 요청한 유저의 정보가 탈퇴 등과 같은 이유로 존재하지 않을 때
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR)
        );
        // [예외처리] 조회하고자 하는 게시물이 삭제 등과 같은 이유로 존재하지 않을 때
        Mentoring mentoring = mentoringRepository.findById(mentoringId).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_POST_ERROR)
        );
        // [예외처리] 신청했던 프로젝트에 다시 신청하는 경우
        if(teamQueryRepository.existsByMentoringIdAndUserId(mentoringId, userId)){
            throw new BadRequestException(ErrorCode.ALREADY_TEAM_ERROR);
        }
        if(mentoringApplicantRepository.existsByUserAndMentoring_Id(user, mentoringId)) {
            throw new BadRequestException(ErrorCode.ALREADY_APPLY_POST_ERROR);
        }

        MentoringApplicant mentoringApplicant = new MentoringApplicant(user, mentoring, dto);
        mentoringApplicantRepository.save(mentoringApplicant);
    }

    @Transactional
    public List<MenteeListResponseDto> getMentoringApplicant(Mentoring mentoring) {
        return mentoringApplicantRepository.findAllByMentoring(mentoring)
                .stream().map(MenteeListResponseDto::new).collect(Collectors.toCollection(ArrayList::new));
    }
}
