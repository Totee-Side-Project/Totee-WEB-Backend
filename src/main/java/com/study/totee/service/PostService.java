package com.study.totee.service;

import com.study.totee.dto.PostDTO;
import com.study.totee.model.PostEntity;
import com.study.totee.model.UserEntity;
import com.study.totee.persistence.PostRepository;
import com.study.totee.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public void save(PostEntity postEntity) throws IOException {
        postRepository.save(postEntity);
    }

    @Transactional(readOnly = true)
    public Page findPostAll(final Pageable pageable){

        return postRepository.findAll(pageable);
    }
}
