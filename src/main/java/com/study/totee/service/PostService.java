package com.study.totee.service;

import com.study.totee.dto.PostDTO;
import com.study.totee.model.CategoryEntity;
import com.study.totee.model.PostEntity;
import com.study.totee.model.UserEntity;
import com.study.totee.persistence.CategoryRepository;
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
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    @Transactional
    public void save(PostEntity postEntity) throws IOException {
        postRepository.save(postEntity);
    }

    @Transactional(readOnly = true)
    public Page<PostEntity> findPostAll(final Pageable pageable){
        return postRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public PostEntity findByPostId(long postId){
        return postRepository.findByPostId(postId);
    }

    @Transactional
    public void updateView(Long postId){
        PostEntity post = postRepository.findByPostId(postId);
        post.setView(post.getView()+1);
    }

    @Transactional
    public PostEntity update(PostDTO postDTO, Long postId, String userId){
        Optional<UserEntity> user = userRepository.findById(userId);
        PostEntity post = postRepository.findByPostIdAndUser(postId, user.get());
        post.setContent(postDTO.getContent());
        post.setTitle(postDTO.getTitle());
        return post;
    }

    @Transactional
    public void delete(Long postId, String userId){
        Optional<UserEntity> user = userRepository.findById(userId);
        PostEntity post = postRepository.findByPostIdAndUser(postId, user.get());
        postRepository.delete(post);
    }

    // 모든 게시글 카테고리 별 조회
    @Transactional(readOnly = true)
    public Page<PostEntity> findPostAllByCategoryName(String categoryName, final Pageable pageable){
        Optional<CategoryEntity> category = Optional.of(categoryRepository.findByCategoryName(categoryName).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 카테고리 입니다.")));
        return postRepository.findAllByCategory_CategoryName(categoryName, pageable);
    }
}
