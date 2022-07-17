package com.study.totee.api.controller;

import com.google.common.collect.Lists;
import com.study.totee.api.dto.comment.CommentResponseDto;
import com.study.totee.api.dto.post.PostRequestDto;
import com.study.totee.api.dto.post.PostResponseDto;
import com.study.totee.common.ApiResponse;
import com.study.totee.api.model.CommentEntity;
import com.study.totee.api.model.PostEntity;
import com.study.totee.api.model.UserEntity;
import com.study.totee.api.service.CommentService;
import com.study.totee.api.service.PostService;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.ForbiddenException;
import com.study.totee.exption.NoAuthException;
import com.study.totee.utils.PositionConverter;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final PositionConverter positionConverter;
    private final ModelMapper modelMapper;

    @ApiOperation(value = "게시글 등록" , notes = "게시글을 등록합니다 (폼데이터 형식 필수)")
    @PostMapping("/api/v1/post")
    public ApiResponse savePost(@AuthenticationPrincipal User principal, @ModelAttribute @Valid @RequestBody PostRequestDto postRequestDto) throws IOException {
        // 로그인 정보가 없으면 예외 발생
        String id = Optional.ofNullable(principal).orElseThrow(() -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();
        postService.save(id, postRequestDto);
        return ApiResponse.success("message" , "게시글이 성공적으로 등록되었습니다.");
    }

    @ApiOperation(value = "게시글 업데이트", notes = "게시글을 수정합니다")
    @PutMapping("/api/v1/post/{postId}")
    public ApiResponse postUpdate(@AuthenticationPrincipal User principal, @RequestBody @ModelAttribute @Valid PostRequestDto postRequestDto, @PathVariable Long postId) throws IOException {
        // 로그인 정보가 없으면 예외 발생
        String id = Optional.ofNullable(principal).orElseThrow(() -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();
        postService.update(id, postRequestDto, postId);

        return ApiResponse.success("message", "게시글이 성공적으로 업데이트되었습니다.");
    }

    @ApiOperation(value = "게시글 삭제" , notes = "게시글을 삭제합니다")
    @DeleteMapping("/api/v1/post/{postId}")
    public ApiResponse deletePost(@AuthenticationPrincipal User principal, @PathVariable Long postId) {
        // 로그인 정보가 없으면 예외 발생
        String id = Optional.ofNullable(principal).orElseThrow(() -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();
        postService.delete(postId, id);
        return ApiResponse.success("message", "게시글이 성공적으로 삭제되었습니다.");
    }

    @ApiOperation(value = "전체 글 목록 불러오기",
            notes = "글 목록 불러오기 ex : api/v1/post/list?page=0&size=5&sort=postId.desc\n" +
                    "page : 몇번째 page 불러올건지\n" +
                    "size : 1페이지 당 개수\n" +
                    "sort : 어떤것을 기준으로 정렬 할 것 인지\n" +
                    "default : Size  16 , sort postId\n")
    @GetMapping("/api/v1/post/list")
    public ApiResponse findPostAll( @PageableDefault(size = 16 ,sort = "postId",direction = Sort.Direction.DESC ) Pageable pageable){
        Page<PostEntity> page = postService.findPostAll(pageable);
        Page<PostResponseDto> map = page.map(post -> new PostResponseDto(post.getPostId(), post.getTitle(), post.getContent(),
                post.getUser().getUserInfo().getNickname(), post.getView(), post.getLikeNum(), post.getCommentNum(),
                null, post.getUser().getProfileImageUrl(), post.getCreated_at(), post.getOnlineOrOffline(), post.getPeriod(),
                post.getStatus(), post.getCategory().getCategoryName(), positionConverter.convertPositionEntityToString(post.getPositionList()), post.getRecruitNum(), post.getContactMethod(),
                post.getContactLink(), post.getUser().getUserInfo().getPosition()));

        return ApiResponse.success("data", map);
    }

    @ApiOperation(value = "게시물 제목 검색합니다.", notes = "제목에 해당하는 게시글을 조회합니다. 빈 검색어 보내면 안됩니다")
    @GetMapping("/api/v1/post/search/{title}")
    public ApiResponse findPostAllByTitle(@PathVariable String title, @PageableDefault
            (size = 16, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable){
        Page<PostEntity> page = postService.searchTitle(title, pageable);
        Page<PostResponseDto> map = page.map(post -> new PostResponseDto(post.getPostId(), post.getTitle(), post.getContent(),
                post.getUser().getUserInfo().getNickname(), post.getView(), post.getLikeNum(), post.getCommentNum(),
                null, post.getUser().getProfileImageUrl(), post.getCreated_at(), post.getOnlineOrOffline(), post.getPeriod(),
                post.getStatus(), post.getCategory().getCategoryName(), positionConverter.convertPositionEntityToString(post.getPositionList()), post.getRecruitNum(), post.getContactMethod(),
                post.getContactLink(), post.getUser().getUserInfo().getPosition()));

        return ApiResponse.success("data", map);
    }

    @ApiOperation(value = "추천 게시물 목록 불러오기", notes = "로그인한 유저의 포지션과 등록된 게시글의 모집분야가 같은 글을 조회합니다.")
    @GetMapping("/api/v1/post/recommend")
    public ApiResponse findPostRecommend(@AuthenticationPrincipal User principal, @PageableDefault(size = 16, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable){
        // 로그인 안한 유저도 추천 게시물 이외의 다른 게시물을 불러와야하기에 예외처리를 하지 않습니다.
        Page<PostEntity> page = postService.findByPosition(principal.getUsername(), pageable);
        Page<PostResponseDto> map = page.map(post -> new PostResponseDto(post.getPostId(), post.getTitle(), post.getContent(),
                post.getUser().getUserInfo().getNickname(), post.getView(), post.getLikeNum(), post.getCommentNum(),
                null, post.getUser().getProfileImageUrl(), post.getCreated_at(), post.getOnlineOrOffline(), post.getPeriod(),
                post.getStatus(), post.getCategory().getCategoryName(), positionConverter.convertPositionEntityToString(post.getPositionList()), post.getRecruitNum(), post.getContactMethod(),
                post.getContactLink(), post.getUser().getUserInfo().getPosition()));

        return ApiResponse.success("data", map);
    }


    @ApiOperation(value = "post 상세보기",
            notes = "PostId로 상세보기\n" +
                    "api 주소에 PathVariable 주면 됩니다.")
    @GetMapping("/api/v1/post/{postId}")
    public ApiResponse findByPostId(@PathVariable Long postId, HttpServletRequest request, HttpServletResponse response){
        PostEntity post = postService.findByPostId(postId);
        if(post == null){
            throw new BadRequestException(ErrorCode.NO_POST_ERROR);
        }
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
            if (!oldCookie.getValue().contains("[" + postId.toString() + "]")) {
                postService.updateView(postId);
                oldCookie.setValue(oldCookie.getValue() + "_[" + postId + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(oldCookie);
            }
        } else {
            postService.updateView(postId);
            Cookie newCookie = new Cookie("postView","[" + postId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }
        List<CommentEntity> commentEntities = commentService.commentListByPostId(postId);
        List<CommentResponseDto> commentDTOList = commentEntities.stream().map(commentEntity ->
                modelMapper.map(commentEntity, CommentResponseDto.class)).collect(Collectors.toList());
        UserEntity user = post.getUser();

        PostResponseDto postResponseDto = PostResponseDto
                .builder()
                .postId(post.getPostId())
                .view(post.getView())
                .title(post.getTitle())
                .author(user.getUserInfo().getNickname())
                .content(post.getContent())
                .categoryName(post.getCategory().getCategoryName())
                .likeNum(post.getLikeNum())
                .commentNum(post.getCommentNum())
                .commentDTOList(commentDTOList)
                .createdAt(post.getCreated_at())
                .status(post.getStatus())
                .imageUrl(post.getUser().getProfileImageUrl())
                .onlineOrOffline(post.getOnlineOrOffline())
                .period(post.getPeriod())
                .recruitNum(post.getRecruitNum())
                .contactLink(post.getContactLink())
                .contactMethod(post.getContactMethod())
                .authorPosition(post.getUser().getUserInfo().getPosition())
                .positionList(positionConverter.convertPositionEntityToString(post.getPositionList()))
                .build();

        return ApiResponse.success("data", postResponseDto);
    }
}
