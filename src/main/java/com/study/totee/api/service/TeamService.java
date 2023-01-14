package com.study.totee.api.service;

import com.study.totee.api.dto.team.MemberListResponseDto;
import com.study.totee.api.model.*;
import com.study.totee.api.persistence.*;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamQueryRepository teamQueryRepository;
    private final ApplicantRepository applicantRepository;
    private final ApplicantQueryRepository applicantQueryRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public boolean AcceptApplication(Post post, User user, Boolean accept){
        Applicant applicant = Optional.ofNullable(applicantQueryRepository.findByUserAndPost(user, post)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_APPLICANT_ERROR)
        );

        if (accept && post.getMemberNum() == post.getRecruitNum() + 1){
            throw new BadRequestException(ErrorCode.NO_APPLICANT_ERROR);
        }

        if (teamQueryRepository.existsByPostIdAndUserId(post.getId(), user.getId())) {
            throw new BadRequestException(ErrorCode.ALREADY_TEAM_ERROR);
        }

        Notification notification = new Notification(applicant, accept);
        notificationRepository.save(notification);
        applicant.deleteApply();
        applicantRepository.delete(applicant);

        if(accept) {
            Team team = new Team(user, post);
            teamRepository.save(team);
            user.getUserInfo().increaseStudyNum();
            return true;
        }
        return false;
    }

    @Transactional
    public List<MemberListResponseDto> getMember(Long postId) {
        return teamQueryRepository.findAllByPostId(postId)
                .stream().map(MemberListResponseDto::new).collect(Collectors.toCollection(ArrayList::new));
    }

    @Transactional
    public void memberDelete(User user, Post post) {
        if (post.getUser().getId().equals(user.getId())) {
            throw new BadRequestException(ErrorCode.NOT_AVAILABLE_ACCESS);
        } else if (post.getStatus().equals("N")) {
            Team team = teamRepository.findByUserAndPost(user, post).orElseThrow(
                    () -> new ForbiddenException(ErrorCode.NO_TEAM_ERROR)
            );
            team.deleteStudyTeam();
            user.getUserInfo().decreaseStudyNum();
            teamRepository.deleteByUserAndPost(user, post);
        } else throw new BadRequestException(ErrorCode.NOT_AVAILABLE_ACCESS);
    }
}
