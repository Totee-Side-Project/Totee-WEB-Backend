package com.study.totee.api.service;

import com.study.totee.api.dto.post.PostRequestDto;
import com.study.totee.api.model.CategoryEntity;
import com.study.totee.api.model.PositionEntity;
import com.study.totee.api.model.PostEntity;
import com.study.totee.api.model.UserEntity;
import com.study.totee.api.persistence.CategoryRepository;
import com.study.totee.api.persistence.PositionRepository;
import com.study.totee.api.persistence.PostRepository;
import com.study.totee.api.persistence.UserRepository;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.ForbiddenException;
import com.study.totee.utils.PositionConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PositionRepository positionRepository;
    private final PositionConverter positionConverter;
    private final AwsS3Service awsS3Service;

    @Transactional
    public void save(String userId, PostRequestDto postRequestDto) throws IOException {
        UserEntity user = userRepository.findById(userId);
        if (user == null){
            throw new ForbiddenException(ErrorCode.NO_AUTHENTICATION_ERROR);
        }
        CategoryEntity category = categoryRepository.findByCategoryName(postRequestDto.getCategoryName())
                .orElseThrow(()-> new BadRequestException(ErrorCode.NO_CATEGORY_ERROR));

        PostEntity post = PostEntity.builder()
                .status("Y").category(category).title(postRequestDto.getTitle()).user(user)
                .content(postRequestDto.getContent()).onlineOrOffline(postRequestDto.getOnlineOrOffline())
                .period(postRequestDto.getPeriod()).view(0).positionList(new HashSet<>()).build();

        if(postRequestDto.getPostImage() != null){
            post.setImageUrl(awsS3Service.upload(postRequestDto.getPostImage(), "static"));
        }
        postRepository.save(post);
        Set<String> positionStringList = new HashSet<>(postRequestDto.getPositionList());
        List<PositionEntity> positionList = positionConverter.convertStringToPositionEntity(new ArrayList<>(positionStringList), null, post);
        post.updatePositionList(positionList);
        positionRepository.saveAll(positionList);
    }

    @Transactional
    public void update(String userId, PostRequestDto postRequestDto, Long postId) throws IOException {
        CategoryEntity category = categoryRepository.findByCategoryName(postRequestDto.getCategoryName())
                .orElseThrow(()-> new BadRequestException(ErrorCode.NO_CATEGORY_ERROR));
        UserEntity user = userRepository.findById(userId);
        PostEntity post = postRepository.findByPostIdAndUser(postId, user);

        post.setContent(postRequestDto.getContent());
        post.setTitle(postRequestDto.getTitle());
        post.setStatus(postRequestDto.getStatus());
        post.setOnlineOrOffline(postRequestDto.getOnlineOrOffline());
        post.setPeriod(postRequestDto.getPeriod());
        post.setCategory(category);
        if(postRequestDto.getPostImage() != null){
            awsS3Service.fileDelete(post.getImageUrl());
            post.setImageUrl(awsS3Service.upload(postRequestDto.getPostImage(), "static"));
        }
        Set<String> positionStringList = new HashSet<>(postRequestDto.getPositionList());
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

    // 게시글 제목 검색
    @Transactional
    public Page<PostEntity> searchTitle(String keyword, Pageable pageable){
        if(keyword.length() < 1){
            throw new BadRequestException(ErrorCode.BAD_KEYWORD_ERROR);
        }
        return postRepository.findAllByTitleContaining(keyword, pageable);
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
