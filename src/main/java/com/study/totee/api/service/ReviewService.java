package com.study.totee.api.service;

import com.study.totee.api.dto.review.ReviewRequestDto;
import com.study.totee.api.dto.review.ReviewResponseDto;
import com.study.totee.api.model.Mentoring;
import com.study.totee.api.model.Review;
import com.study.totee.api.model.User;
import com.study.totee.api.persistence.MentoringRepository;
import com.study.totee.api.persistence.ReviewRepository;
import com.study.totee.api.persistence.TeamRepository;
import com.study.totee.api.persistence.UserRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MentoringRepository mentoringRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public void save(String userId, ReviewRequestDto dto, Long mentoringId){
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                () -> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));
        Mentoring mentoring = mentoringRepository.findById(mentoringId).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_POST_ERROR));
        // 해당 멘토링의 멘티가 아닐 때 예외 처리
        teamRepository.findByUserAndMentoring(user, mentoring).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_TEAM_ERROR)
        );
        // 리뷰를 이미 남겼을 때 예외 처리
        if(reviewRepository.existsByUserAndMentoring(user, mentoring)){
            throw new BadRequestException(ErrorCode.ALREADY_EXIST_REVIEW);
        }

        Review savedReview = reviewRepository.save(new Review(user, mentoring, dto));
        mentoring.addReview(savedReview);
    }

    @Transactional
    public Page<ReviewResponseDto> findAll(Long mentoringId, Pageable pageable){
        Mentoring mentoring = mentoringRepository.findById(mentoringId).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_POST_ERROR));
        return reviewRepository.findAllByMentoring(pageable, mentoring).map(ReviewResponseDto::new);
    }
}
