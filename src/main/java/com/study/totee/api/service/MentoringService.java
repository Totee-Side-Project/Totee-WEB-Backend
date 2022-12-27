package com.study.totee.api.service;

import com.study.totee.api.dto.mentoring.MentoringRequestDto;
import com.study.totee.api.dto.post.PostRequestDto;
import com.study.totee.api.model.*;
import com.study.totee.api.persistence.MentorRepository;
import com.study.totee.api.persistence.MentoringRepository;
import com.study.totee.api.persistence.UserRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import com.study.totee.type.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
    public void save(String userId, MentoringRequestDto requestDto) throws IOException {
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        if(!user.getRoleType().equals(RoleType.totee)){
            throw new BadRequestException(ErrorCode.NO_AUTHORIZATION_ERROR);
        }

        mentoringRepository.save(new Mentoring(requestDto, user));
    }
}
