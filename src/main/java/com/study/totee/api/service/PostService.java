package com.study.totee.api.service;

import com.study.totee.api.dto.post.PostRequestDto;
import com.study.totee.api.model.CategoryEntity;
import com.study.totee.api.model.PostEntity;
import com.study.totee.api.model.UserEntity;
import com.study.totee.api.persistence.CategoryRepository;
import com.study.totee.api.persistence.PostRepository;
import com.study.totee.api.persistence.UserRepository;
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
    private final AwsS3Service awsS3Service;

    @Transactional
    public void save(String userId, PostRequestDto postRequestDto) throws IOException {
        CategoryEntity category = categoryRepository.findByCategoryName(postRequestDto.getCategoryName())
                .orElseThrow(()-> new IllegalArgumentException("찾을 수 없는 카테고리 입니다."));
        UserEntity user = userRepository.findById(userId);

        PostEntity post = PostEntity.builder()
            .status("Y").category(category).title(postRequestDto.getTitle()).user(user)
            .content(postRequestDto.getContent()).onlineOrOffline(postRequestDto.getOnlineOrOffline())
            .period(postRequestDto.getPeriod()).target(postRequestDto.getTarget()).view(0).build();

        if(postRequestDto.getPostImage() != null){
            post.setImageUrl(awsS3Service.upload(postRequestDto.getPostImage(), "static"));
        }

        postRepository.save(post);
    }

    @Transactional
    public void update(String userId, PostRequestDto postRequestDto, Long postId) throws IOException {
        CategoryEntity category = categoryRepository.findByCategoryName(postRequestDto.getCategoryName())
                .orElseThrow(()-> new IllegalArgumentException("찾을 수 없는 카테고리 입니다."));
        UserEntity user = userRepository.findById(userId);
        PostEntity post = postRepository.findByPostIdAndUser(postId, user);

        post.setContent(postRequestDto.getContent());
        post.setTitle(postRequestDto.getTitle());
        post.setStatus(postRequestDto.getStatus());
        post.setOnlineOrOffline(postRequestDto.getOnlineOrOffline());
        post.setPeriod(postRequestDto.getPeriod());
        post.setTarget(postRequestDto.getTarget());
        post.setCategory(category);
        if(postRequestDto.getPostImage() != null){
            awsS3Service.fileDelete(post.getImageUrl());
            post.setImageUrl(awsS3Service.upload(postRequestDto.getPostImage(), "static"));
        }
    }

    @Transactional
    public void delete(Long postId, String userId){
        UserEntity user = userRepository.findById(userId);
        PostEntity post = postRepository.findByPostIdAndUser(postId, user);
        if(post.getImageUrl() != null){
            awsS3Service.fileDelete(post.getImageUrl());
        }
        postRepository.delete(post);
    }

    // 모든 게시글 카테고리 별 조회
    @Transactional(readOnly = true)
    public Page<PostEntity> findPostAllByCategoryName(String categoryName, final Pageable pageable){
        Optional<CategoryEntity> category = Optional.of(categoryRepository.findByCategoryName(categoryName).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 카테고리 입니다.")));
        return postRepository.findAllByCategory_CategoryName(categoryName, pageable);
    }

    // 모든 게시글 모집중인 글만 조회
    @Transactional(readOnly = true)
    public Page<PostEntity> findPostAllByStatus(Pageable pageable){
        return postRepository.findAllByStatus("Y", pageable);
    }

    // 모든 게시글 카테고리 별, 모집중인 글만 조회
    @Transactional(readOnly = true)
    public Page<PostEntity> findAllByCategory_CategoryNameAndStatus(String categoryName, Pageable pageable){
        Optional<CategoryEntity> category = Optional.of(categoryRepository.findByCategoryName(categoryName).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 카테고리 입니다.")));
        return postRepository.findAllByCategory_CategoryNameAndStatus(categoryName, "Y", pageable);
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
}
