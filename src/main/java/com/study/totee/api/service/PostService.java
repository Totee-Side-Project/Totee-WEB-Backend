package com.study.totee.api.service;

import com.google.common.net.HttpHeaders;
import com.study.totee.api.dto.comment.CommentResponseDto;
import com.study.totee.api.dto.post.PostRequestDto;
import com.study.totee.api.dto.post.PostResponseDto;
import com.study.totee.api.model.*;
import com.study.totee.api.persistence.*;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.ForbiddenException;
import com.study.totee.type.PeriodType;
import com.study.totee.utils.CookieUtil;
import com.study.totee.utils.PositionConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PositionRepository positionRepository;
    private final NotificationRepository notificationRepository;
    private final PositionConverter positionConverter;
    private final AwsS3Service awsS3Service;

    @Transactional
    public void save(String userId, PostRequestDto postRequestDto) throws IOException {
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        Category category = Optional.ofNullable(categoryRepository.findByCategoryName(postRequestDto.getCategoryName())).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_CATEGORY_ERROR));

        Post savedPost = postRepository.save(new Post(user, category, postRequestDto));

        Set<String> positionStringList = new HashSet<>(postRequestDto.getPositionList());
        List<Position> positionList = positionConverter.convertStringToPositionEntity(new ArrayList<>(positionStringList), null, savedPost);
        savedPost.setPositionList(new HashSet<>(positionList));

//        게시글의 썸네일 디자인이 생길 시 추가!
//        if(postRequestDto.getPostImage() != null){
//            post.setImageUrl(awsS3Service.upload(postRequestDto.getPostImage(), "static"));
//        }

        positionRepository.saveAll(positionList);
    }

    // 게시글 상세 조회
    @Transactional
    public PostResponseDto findByPostId(long postId, List<CommentResponseDto> commentDTOList,
                                        HttpServletRequest request, HttpServletResponse response){

        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));

        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + postId + "]")) {
                post.increaseView();
                oldCookie.setValue(oldCookie.getValue() + "_[" + postId + "]");
                oldCookie.setPath("/");
                oldCookie.setHttpOnly(true);
                oldCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(oldCookie);
                response.addHeader("Set-Cookie", oldCookie.getName() + "=" + oldCookie.getValue() + "; Secure; SameSite=None");
            }
        } else {
            post.increaseView();
            Cookie newCookie = new Cookie("postView","[" + postId + "]");
            newCookie.setPath("/");
            newCookie.setHttpOnly(true);
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
            response.setHeader("Set-Cookie", newCookie.getName() + "=" + newCookie.getValue() + "; Secure; SameSite=None");
        }

        return new PostResponseDto(post, commentDTOList);
    }

    @Transactional
    public void update(String userId, PostRequestDto postRequestDto, Long postId) throws IOException {
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        Category category = Optional.ofNullable(categoryRepository.findByCategoryName(postRequestDto.getCategoryName())).orElseThrow(
                ()-> new BadRequestException(ErrorCode.ALREADY_EXIST_CATEGORY_ERROR));

        Post post = Optional.ofNullable(postRepository.findByIdAndUser(postId, user)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));

//        게시글의 썸네일 디자인이 생길 시 추가!
//        if(postRequestDto.getPostImage() != null){
//            awsS3Service.fileDelete(post.getImageUrl());
//            post.setImageUrl(awsS3Service.upload(postRequestDto.getPostImage(), "static"));
//        }

        // 기존 포지션 리스트 삭제 후 새로운 포지션 리스트 저장
        positionRepository.deleteAllByPostId(post.getId());
        Set<String> positionStringList = new HashSet<>(postRequestDto.getPositionList());
        List<Position> positionEntityList = positionConverter.convertStringToPositionEntity(new ArrayList<>(positionStringList), null, post);

        post.update(postRequestDto, category, positionEntityList);
        positionRepository.saveAll(positionEntityList);
    }

    @Transactional
    public void delete(Long postId, String userId){
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));
        Post post = Optional.ofNullable(postRepository.findByIdAndUser(postId, user)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));

//        게시글의 썸네일 디자인이 생길 시 추가!
//        if(post.getImageUrl() != null){
//            awsS3Service.fileDelete(post.getImageUrl());
//        }

        notificationRepository.deleteAllByPost_Id(post.getId());
        postRepository.delete(post);
    }

    // 게시글 제목 검색
    @Transactional(readOnly = true)
    public Page<PostResponseDto> searchTitle(String keyword, Pageable pageable){
        if(keyword.length() < 1){
            throw new BadRequestException(ErrorCode.BAD_KEYWORD_ERROR);
        }
        return postRepository.findAllByTitleContaining(keyword, pageable).map(PostResponseDto::new);
    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public Page<PostResponseDto> findPostAll(final Pageable pageable){
        return postRepository.findAll(pageable).map(PostResponseDto::new);
    }

    // 내가 작성한 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> findAllByUserId(String userId){
        Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        return postRepository.findAllByUser_Id(userId).stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    // 추천 게시글 전체 조회 (로그인한 유저의 포지션과 등록된 게시글의 모집분야가 같은 글을 조회)
    @Transactional
    public Page<PostResponseDto> findByPosition(String userId, Pageable pageable){
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        return postRepository.findAllByPosition(user.getUserInfo().getPosition(), pageable).map(PostResponseDto::new);
    }

    // 내가 좋아요 한 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> findAllByLikedPost(String userId){
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        return postRepository.findAllByLikedPost(user).stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    // 모집 여부 상태 변경
    @Transactional
    public void updateStatus(String userId, Long postId){
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));

        if(!post.getUser().getId().equals(userId)) {
            throw new ForbiddenException(ErrorCode.NO_AUTHORITY_ERROR);
        }
        post.updateStatus(post.getStatus());
    }

}
