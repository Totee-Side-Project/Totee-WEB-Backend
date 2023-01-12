package com.study.totee.api.service;

import com.study.totee.api.dto.team.MemberListResponseDto;
import com.study.totee.api.model.Applicant;
import com.study.totee.api.model.Notification;
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
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final ApplicantQueryRepository applicantQueryRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TeamQueryRepository teamQueryRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public void applyPost(String userId, Long postId, String message) {

        // [예외처리] 요청한 유저의 정보가 탈퇴 등과 같은 이유로 존재하지 않을 때
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR)
        );
        // [예외처리] 조회하고자 하는 게시물이 삭제 등과 같은 이유로 존재하지 않을 때
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_POST_ERROR)
        );
        // [예외처리] 이미 팀에 속해 있을 경우
        if(teamQueryRepository.existsByPostIdAndUserId(postId, userId)){
            throw new BadRequestException(ErrorCode.ALREADY_TEAM_ERROR);
        }
        // [예외처리] 신청했던 프로젝트에 다시 신청하는 경우
        if(applicantQueryRepository.existsByUserIdAndPostId(user.getUserSeq(), post.getId())) {
            throw new BadRequestException(ErrorCode.ALREADY_APPLY_POST_ERROR);
        }
        // [예외처리] 이미 시작한 프로젝트에 신청할 경우
        if (post.getStatus().equals("N")){
            throw new BadRequestException(ErrorCode.ALREADY_STARTED_ERROR);
        }

        Applicant applicant = Applicant.builder()
                .user(user)
                .post(post)
                .message(message)
                .build();

        Notification notification = new Notification(applicant, user);
        notificationRepository.save(notification);

        applicantRepository.save(applicant);
    }

    @Transactional
    public void cancelApply(String userId, Long postId) {

        // [예외처리] 요청한 유저의 정보가 탈퇴 등과 같은 이유로 존재하지 않을 때
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR)
        );
        // [예외처리] 조회하고자 하는 게시물이 삭제 등과 같은 이유로 존재하지 않을 때
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_POST_ERROR)
        );
        // [예외처리] 대기열에 대기 정보가 없을 때
        Applicant applicant = applicantRepository.findByUserAndPost(user, post).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_APPLICANT_ERROR)
        );

        applicant.deleteApply();
        applicantRepository.delete(applicant);
    }

    @Transactional
    public List<MemberListResponseDto> getApplicant(Post post) {
        return applicantRepository.findAllByPost(post)
                .stream().map(MemberListResponseDto::new).collect(Collectors.toCollection(ArrayList::new));
    }

}
