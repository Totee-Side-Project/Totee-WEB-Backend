package com.study.totee.api.service;

import com.study.totee.api.dto.mentor.MentorRequestDto;
import com.study.totee.api.model.Mentor;
import com.study.totee.api.model.User;
import com.study.totee.api.persistence.MentorRepository;
import com.study.totee.api.persistence.UserRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;
    private final UserRepository userRepository;

    @Transactional
    public void applyMentor(String userId, MentorRequestDto mentorRequestDto){
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        // 멘토 등록 심사 중일 경우
        if(mentorRepository.existsByUser(user)){
            throw new BadRequestException(ErrorCode.ALREADY_EXIST_MENTOR_APPLY);
        }

        mentorRepository.save(new Mentor(mentorRequestDto, user));
    }
}
