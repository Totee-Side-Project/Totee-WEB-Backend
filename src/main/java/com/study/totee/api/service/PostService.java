package com.study.totee.api.service;

import com.study.totee.api.dto.comment.CommentResponseDto;
import com.study.totee.api.dto.post.PostRequestDto;
import com.study.totee.api.dto.post.PostResponseDto;
import com.study.totee.api.model.*;
import com.study.totee.api.persistence.*;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.ForbiddenException;
import com.study.totee.utils.CookieUtil;
import com.study.totee.utils.SkillConverter;
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
    private final TeamRepository teamRepository;
    private final SkillRepository skillRepository;
    private final ApplicantRepository applicantRepository;
    private final NotificationRepository notificationRepository;
    private final SkillConverter skillConverter;

    @Transactional
    public void save(String userId, PostRequestDto postRequestDto) throws IOException {
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));


        Post savedPost = postRepository.save(new Post(user, postRequestDto));

        Set<String> skillStringList = new HashSet<>(postRequestDto.getSkillList());
        List<Skill> skillList = skillConverter.convertStringToSkillEntity(new ArrayList<>(skillStringList), savedPost);
        savedPost.setSkillList(new LinkedHashSet<>(skillList));
        teamRepository.save(new Team(user, savedPost));
//        게시글의 썸네일 디자인이 생길 시 추가!
//        if(postRequestDto.getPostImage() != null){
//            post.setImageUrl(awsS3Service.upload(postRequestDto.getPostImage(), "static"));
//        }

        skillRepository.saveAll(skillList);
    }

    // 게시글 상세 조회
    @Transactional
    public PostResponseDto findByPostId(long postId, List<CommentResponseDto> commentDTOList,
                                        HttpServletRequest request, HttpServletResponse response){

        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));

        Cookie oldCookie = CookieUtil.getCookie(request, "postView").orElse(null);

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + postId + "]")) {
                post.increaseView();
                oldCookie.setValue(oldCookie.getValue() + "_[" + postId + "]");
                CookieUtil.addSameSiteCookie(response, oldCookie.getName(), oldCookie.getValue(), 60 * 60 * 24);
            }
        } else {
            post.increaseView();
            CookieUtil.addSameSiteCookie(response, "postView", "[" + postId + "]", 60 * 60 * 24);
        }

        return new PostResponseDto(post, commentDTOList);
    }

    @Transactional
    public void update(String userId, PostRequestDto postRequestDto, Long postId) throws IOException {
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));


        Post post = Optional.ofNullable(postRepository.findByIdAndUser(postId, user)).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NO_POST_ERROR));

//        게시글의 썸네일 디자인이 생길 시 추가!
//        if(postRequestDto.getPostImage() != null){
//            awsS3Service.fileDelete(post.getImageUrl());
//            post.setImageUrl(awsS3Service.upload(postRequestDto.getPostImage(), "static"));
//        }

        // 기존 기술 리스트 삭제 후 새로운 리스트 저장
        skillRepository.deleteAllByPostId(post.getId());
        Set<String> skillStringList = new LinkedHashSet<>(postRequestDto.getSkillList());
        List<Skill> skillEntityList = skillConverter.convertStringToSkillEntity(new ArrayList<>(skillStringList), post);

        post.update(postRequestDto, skillEntityList);
        skillRepository.saveAll(skillEntityList);
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
        applicantRepository.deleteAllByPost(post);
        teamRepository.deleteAllByPost(post);
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

    public Post findByPostId(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new BadRequestException(ErrorCode.NO_POST_ERROR)
        );
    }

    public Post loadPostIfOwner(Long postId, User user) {
        return Optional.ofNullable(postRepository.findByIdAndUser(postId, user)).orElseThrow(
                () -> new ForbiddenException(ErrorCode.NO_AUTHORIZATION_ERROR)
        );
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
