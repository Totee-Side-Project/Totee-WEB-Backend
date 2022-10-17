package com.study.totee.api.service;

import com.study.totee.api.dto.team.MemberListResponseDto;
import com.study.totee.api.model.Applicant;
import com.study.totee.api.model.Post;
import com.study.totee.api.model.Team;
import com.study.totee.api.model.User;
import com.study.totee.api.persistence.ApplicantQueryRepository;
import com.study.totee.api.persistence.ApplicantRepository;
import com.study.totee.api.persistence.TeamQueryRepository;
import com.study.totee.api.persistence.TeamRepository;
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

    @Transactional
    public boolean AcceptApplication(Post post, User user, Boolean accept){
        Applicant applicant = Optional.ofNullable(applicantQueryRepository.findByUserAndPost(user, post)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_APPLICANT_ERROR)
        );

        if (accept && post.getMemberNum() == post.getRecruitNum()){
            throw new BadRequestException(ErrorCode.NO_APPLICANT_ERROR);
        }

        if (teamQueryRepository.existsByPostIdAndUserId(post.getId(), user.getId())) {
            throw new BadRequestException(ErrorCode.ALREADY_TEAM_ERROR);
        }

        applicant.deleteApply();
        applicantRepository.delete(applicant);

        if(accept) {
            Team team = new Team(user, post);
            teamRepository.save(team);
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
            team.deleteTeam();
            teamRepository.deleteByUserAndPost(user, post);
        } else throw new BadRequestException(ErrorCode.NOT_AVAILABLE_ACCESS);
    }

    public boolean isMember(Post post, User user) {
        return teamRepository.findByUserAndPost(user, post).isPresent();

    }
}
