package com.study.totee.api.service;

import com.study.totee.api.dto.team.MemberListResponseDto;
import com.study.totee.api.dto.team.MenteeListResponseDto;
import com.study.totee.api.dto.user.NicknameRequestDto;
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
    private final UserService userService;
    private final MentoringApplicantRepository mentoringApplicantRepository;

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
    public boolean AcceptApplication(Mentoring mentoring, User user, Boolean accept){
        MentoringApplicant applicant = Optional.ofNullable(mentoringApplicantRepository.findByUserAndMentoring(user, mentoring)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_APPLICANT_ERROR)
        );

        if (teamQueryRepository.existsByMentoringIdAndUserId(mentoring.getId(), user.getId())) {
            throw new BadRequestException(ErrorCode.ALREADY_TEAM_ERROR);
        }

        if(accept) {
            Team team = new Team(user, mentoring, applicant);
            teamRepository.save(team);
            applicant.check();
            user.getUserInfo().increaseMentoringNum();
            mentoring.increaseScore();
            return true;
        }
        applicant.deleteApply();
        mentoringApplicantRepository.delete(applicant);
        return false;
    }

    @Transactional
    public List<MemberListResponseDto> getMember(Long postId) {
        return teamQueryRepository.findAllByPostId(postId)
                .stream().map(MemberListResponseDto::new).collect(Collectors.toCollection(ArrayList::new));
    }

    @Transactional
    public List<MenteeListResponseDto> getMentee(Long mentoringId) {
        return teamQueryRepository.findAllByMentoringId(mentoringId)
                .stream().map(MenteeListResponseDto::new).collect(Collectors.toCollection(ArrayList::new));
    }

    @Transactional
    public void memberDelete(User user, Post post, NicknameRequestDto dto) {
        if (!post.getUser().getId().equals(user.getId())) {
            throw new BadRequestException(ErrorCode.NOT_AVAILABLE_ACCESS);
        }
        User member = userService.getUserByNickname(dto.getNickname());
        if (member.getId().equals(user.getId())){
            throw new BadRequestException(ErrorCode.NOT_EXPEL_ERROR);
        }
        Team team = teamRepository.findByUserAndPost(member, post).orElseThrow(
                () -> new ForbiddenException(ErrorCode.NO_TEAM_ERROR)
        );
        team.deleteStudyTeam();
        member.getUserInfo().decreaseStudyNum();
        teamRepository.deleteByUserAndPost(member, post);

    }

    @Transactional
    public void memberDelete(User user, Post post) {
        if(user.getId().equals(post.getUser().getId())){
            throw new BadRequestException(ErrorCode.NOT_EXPEL_ERROR);
        }
        Team team = teamRepository.findByUserAndPost(user, post).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_TEAM_ERROR)
        );
        team.deleteStudyTeam();
        user.getUserInfo().decreaseStudyNum();
        teamRepository.deleteByUserAndPost(user, post);
    }
}
