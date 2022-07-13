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
import com.study.totee.type.PeriodType;
import com.study.totee.utils.PositionConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
        UserEntity user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_USER_ERROR));

        CategoryEntity category = categoryRepository.findByCategoryName(postRequestDto.getCategoryName())
                .orElseThrow(()-> new BadRequestException(ErrorCode.NO_CATEGORY_ERROR));

        PostEntity post = PostEntity.builder()
                .status("Y").category(category).title(postRequestDto.getTitle()).user(user)
                .content(postRequestDto.getContent()).onlineOrOffline(postRequestDto.getOnlineOrOffline())
                .period(PeriodType.of(postRequestDto.getPeriod())).view(0).positionList(new HashSet<>())
                .contactLink(postRequestDto.getContactLink()).contactMethod(postRequestDto.getContactMethod())
                .build();

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
        UserEntity user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_USER_ERROR));
        CategoryEntity category = categoryRepository.findByCategoryName(postRequestDto.getCategoryName())
                .orElseThrow(()-> new BadRequestException(ErrorCode.NO_CATEGORY_ERROR));
        PostEntity post = Optional.ofNullable(postRepository.findByPostIdAndUser(postId, user)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));
        positionRepository.deleteAllByPost(post);
        Set<String> positionStringList = new HashSet<>(postRequestDto.getPositionList());
        List<PositionEntity> positionEntityList = positionConverter.convertStringToPositionEntity(new ArrayList<>(positionStringList), null, post);
        post.update(postRequestDto, category);
        post.updatePositionList(positionEntityList);
        positionRepository.saveAll(positionEntityList);

        if(postRequestDto.getPostImage() != null){
            awsS3Service.fileDelete(post.getImageUrl());
            post.setImageUrl(awsS3Service.upload(postRequestDto.getPostImage(), "static"));
        }

    }

    @Transactional
    public void delete(Long postId, String userId){
        UserEntity user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_USER_ERROR));
        PostEntity post = Optional.ofNullable(postRepository.findByPostIdAndUser(postId, user)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));

        if(post.getImageUrl() != null){
            awsS3Service.fileDelete(post.getImageUrl());
        }
        postRepository.delete(post);
    }

    // 게시글 제목 검색
    @Transactional(readOnly = true)
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

    // 로그인한 유저의 포지션과 등록된 게시글의 모집분야가 같은 글을 조회
    @Transactional
    public Page<PostEntity> findByPosition(String userId, Pageable pageable){
        UserEntity user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_USER_ERROR));
        if (user.getUserInfo().getPosition() != null){
            return postRepository.findAllByPosition(user.getUserInfo().getPosition(), pageable);
        }
        return postRepository.findAllByCategory_CategoryNameAndStatus("프로젝트", "Y", pageable);
    }
}
