package com.study.totee.api.controller;

import com.study.totee.api.dto.comment.CommentResponseDto;
import com.study.totee.api.dto.post.PostRequestDto;
import com.study.totee.api.dto.post.PostResponseDto;
import com.study.totee.common.ApiResponse;
import com.study.totee.api.service.CommentService;
import com.study.totee.api.service.PostService;
import com.study.totee.exption.ErrorCode;
import com.study.totee.exption.NoAuthException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    private final ModelMapper modelMapper;

    @ApiOperation(value = "게시글 등록" , notes = "게시글을 등록합니다 (폼데이터 형식 필수)")
    @PostMapping("/api/v1/post")
    public ApiResponse<Object> createPost(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, @ModelAttribute @Valid @RequestBody PostRequestDto postRequestDto) throws IOException {
        // 로그인 정보가 없으면 예외 발생
        String id = Optional.ofNullable(principal).orElseThrow(() -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();

        postService.save(id, postRequestDto);

        return ApiResponse.success("message" , "게시글이 성공적으로 등록되었습니다.");
    }

    @ApiOperation(value = "게시글 업데이트", notes = "게시글을 수정합니다")
    @PutMapping("/api/v1/post/{postId}")
    public ApiResponse<Object> updatePost(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, @RequestBody @ModelAttribute @Valid PostRequestDto postRequestDto, @PathVariable Long postId) throws IOException {
        // 로그인 정보가 없으면 예외 발생
        String id = Optional.ofNullable(principal).orElseThrow(() -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();

        postService.update(id, postRequestDto, postId);

        return ApiResponse.success("message", "게시글이 성공적으로 업데이트되었습니다.");
    }

    @ApiOperation(value = "게시글 삭제" , notes = "게시글을 삭제합니다")
    @DeleteMapping("/api/v1/post/{postId}")
    public ApiResponse<Object> deletePost(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, @PathVariable Long postId) {
        // 로그인 정보가 없으면 예외 발생
        String id = Optional.ofNullable(principal).orElseThrow(() -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();

        postService.delete(postId, id);

        return ApiResponse.success("message", "게시글이 성공적으로 삭제되었습니다.");
    }

    @ApiOperation(value = "전체 글 목록 불러오기",
            notes = "글 목록 불러오기 ex : api/v1/post/list?page=0&size=5&sort=postId.desc")
    @GetMapping("/api/v1/post/list")
    public ApiResponse<Object> findPostAll( @PageableDefault(size = 16 ,sort = "id",direction = Sort.Direction.DESC ) Pageable pageable){
        Page<PostResponseDto> page = postService.findPostAll(pageable);

        return ApiResponse.success("data", page);
    }

    @ApiOperation(value = "게시물 제목 검색합니다.", notes = "제목에 해당하는 게시글을 조회합니다. 빈 검색어 보내면 안됩니다")
    @GetMapping("/api/v2/post/search/{title}")
    public ApiResponse<Object> findPostAllByTitle(@PathVariable String title, @PageableDefault
            (size = 16, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<PostResponseDto> page = postService.searchTitle(title, pageable);

        return ApiResponse.success("data", page);
    }

    @ApiOperation(value = "추천 게시물 목록 불러오기", notes = "로그인한 유저의 포지션과 등록된 게시글의 모집분야가 같은 글을 조회합니다.")
    @GetMapping("/api/v1/post/recommend")
    public ApiResponse<Object> findPostRecommend(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        if(principal == null){
            return ApiResponse.success("data", postService.findPostAll(pageable));
        }
        Page<PostResponseDto> page = postService.findByPosition(principal.getUsername(), pageable);

        return ApiResponse.success("data", page);
    }

    @ApiOperation(value = "내가 작성한 게시글 목록 불러오기", notes = "로그인한 유저의 게시글을 모두 조회합니다.")
    @GetMapping("/api/v1/post/mypost")
    public ApiResponse<Object> findPostMyPost(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal){
        // 로그인 정보가 없으면 예외 발생
        String id = Optional.ofNullable(principal).orElseThrow(() -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();

        List<PostResponseDto> page = postService.findAllByUserId(id);

        return ApiResponse.success("data", page);
    }

    @ApiOperation(value = "좋아요한 글 리스트", notes = "로그인 한 유저의 좋아요 누른 글의 리스트를 조회합니다")
    @GetMapping("/api/v1/post/like")
    public ApiResponse<Object> myLikePost(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal){
        // 로그인 정보가 없으면 예외 발생
        String id = Optional.ofNullable(principal).orElseThrow(() -> new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();

        List<PostResponseDto> page = postService.findAllByLikedPost(id);

        return ApiResponse.success("data", page);
    }


    @ApiOperation(value = "post 상세보기",
            notes = "PostId로 상세보기\n" +
                    "api 주소에 PathVariable 주면 됩니다.")
    @GetMapping("/api/v1/post/{postId}")
    public ApiResponse<Object> getPost(@PathVariable Long postId, HttpServletRequest request, HttpServletResponse response){

        // 포스트에 속한 댓글과 답글을 불러와 Dto 에 담습니다.
        List<CommentResponseDto> commentDTOList = commentService.commentListByPostId(postId).stream().map(commentEntity ->
                modelMapper.map(commentEntity, CommentResponseDto.class)).collect(Collectors.toList());

        PostResponseDto post = postService.findByPostId(postId, commentDTOList, request, response);

        return ApiResponse.success("data", post);
    }

    @ApiOperation(value = "모집완료 수정", notes = "모집 중일 때 모집완료, 모집완료 일 때 모집 중으로 변환")
    @PostMapping("/api/v1/post/status/{postId}")
    public ApiResponse<Object> updateStatus(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal , @PathVariable Long postId) {

        String id = Optional.ofNullable(principal).orElseThrow(()->
                new NoAuthException(ErrorCode.NO_AUTHENTICATION_ERROR)).getUsername();

        postService.updateStatus(id, postId);

        return ApiResponse.success("data", "게시글이 성공적으로 수정되었습니다");
    }
}
