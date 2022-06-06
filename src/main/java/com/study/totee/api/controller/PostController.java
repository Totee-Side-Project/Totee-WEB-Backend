package com.study.totee.api.controller;

import com.study.totee.common.ApiResponse;
import com.study.totee.api.dto.CommentDTO;
import com.study.totee.api.dto.PostDTO;
import com.study.totee.api.model.CategoryEntity;
import com.study.totee.api.model.CommentEntity;
import com.study.totee.api.model.PostEntity;
import com.study.totee.api.model.UserEntity;
import com.study.totee.api.service.CategoryService;
import com.study.totee.api.service.CommentService;
import com.study.totee.api.service.PostService;
import com.study.totee.api.service.UserService;
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
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @ApiOperation(value = "포스트 등록" , notes = "포스트를 등록합니다")
    @PostMapping("/api/v1/post")
    public ApiResponse savePost(@AuthenticationPrincipal User principal, @RequestBody PostDTO postDTO){
        try {
            UserEntity user = userService.getUser(principal.getUsername());
            Optional<CategoryEntity> category = categoryService.findByCategoryName(postDTO.getCategoryName());
            PostEntity postEntity = PostEntity.builder()
                    .content(postDTO.getContent())
                    .title(postDTO.getTitle())
                    .status("Y")
                    .user(user)
                    .category(category.get())
                    .build();

            postService.save(postEntity);
            return ApiResponse.success("message" , "SUCCESS");
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.fail("message", "Failed");
        }
    }

    @ApiOperation(value = "post 업데이트", notes = "게시글을 수정합니다")
    @PutMapping("/api/v1/post/{postId}")
    public ApiResponse postUpdate(@AuthenticationPrincipal User principal, @RequestBody PostDTO postDTO, @PathVariable Long postId){
        PostEntity post = postService.update(postDTO, postId, principal.getUsername());
        PostDTO response = PostDTO.builder()
                .postId(post.getPostId())
                .view(post.getView())
                .title(post.getTitle())
                .content(post.getContent())
                .categoryName(post.getCategory().getCategoryName())
                .likeCount(post.getLike().size())
                .commentCount(post.getComment().size())
                .status(post.getStatus())
                .build();
        return ApiResponse.success("data", response);
    }

    @ApiOperation(value = "post 삭제" , notes = "게시글을 삭제합니다")
    @DeleteMapping("/api/v1/post/{postId}")
    public ApiResponse deletePost(@AuthenticationPrincipal User principal, @PathVariable Long postId) {
        postService.delete(postId, principal.getUsername());
        return ApiResponse.success("message", "SUCCESS");
    }

    @ApiOperation(value = "전체 글 목록 불러오기",
            notes = "글 목록 불러오기 ex : api/v1/post/list?page=0&size=5&sort=postId.desc\n" +
                    "page : 몇번째 page 불러올건지\n" +
                    "size : 1페이지 당 개수\n" +
                    "sort : 어떤것을 기준으로 정렬 할 것 인지\n" +
                    "default : Size  16 , sort postId\n" +
                    "임시저장글은 불러오지 않음\n")
    @GetMapping("/api/v1/post/list")
    public ApiResponse findPostAll( @PageableDefault(size = 16 ,sort = "postId",direction = Sort.Direction.DESC ) Pageable pageable){
        Page<PostEntity> page = postService.findPostAll(pageable);
        Page<PostDTO> map = page.map(post -> new PostDTO(post.getUser().getUsername(), post.getView(), post.getPostId(), post.getCreated_at(),
                post.getUser().getUserInfo().getMajor(), post.getTitle(), post.getContent(),
                post.getCategory().getCategoryName(), post.getLike().size(), post.getStatus(), post.getComment().size(), null));
        return ApiResponse.success("data", map);
    }

    @ApiOperation(value = "카테고리 별 글 목록 불러오기", notes = "카테고리 별 모든 게시글을 조회합니다.")
    @GetMapping("/api/v1/post/list/{categoryName}")
    public ApiResponse findPostAllByCategoryName(@PathVariable String categoryName, @PageableDefault
            (size = 16, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable){
        Page<PostEntity> page = postService.findPostAllByCategoryName(categoryName, pageable);
        Page<PostDTO> map = page.map(post -> new PostDTO(post.getUser().getUsername(), post.getView(), post.getPostId(), post.getCreated_at(),
                post.getUser().getUserInfo().getMajor(), post.getTitle(), post.getContent(),
                post.getCategory().getCategoryName(), post.getLike().size(), post.getStatus(), post.getComment().size(), null));
        return ApiResponse.success("data", map);
    }

    @ApiOperation(value = "post 상세보기",
            notes = "PostId로 상세보기\n" +
                    "api 주소에 PathVariable 주면 됩니다.")
    @GetMapping("/api/v1/post/{postId}")
    public ApiResponse findByPostId(@PathVariable Long postId, HttpServletRequest request, HttpServletResponse response){
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
        PostEntity post = postService.findByPostId(postId);
        List<CommentEntity> commentEntities = commentService.CommentListByPostId(postId);
        List<CommentDTO> commentDTOList = commentEntities.stream().map(commentEntity ->
                modelMapper.map(commentEntity, CommentDTO.class)).collect(Collectors.toList());
        UserEntity user = post.getUser();

        PostDTO postDTO = PostDTO
                .builder()
                .postId(post.getPostId())
                .view(post.getView())
                .title(post.getTitle())
                .username(user.getUsername())
                .content(post.getContent())
                .categoryName(post.getCategory().getCategoryName())
                .likeCount(post.getLike().size())
                .commentCount(post.getComment().size())
                .commentDTOList(commentDTOList)
                .createdAt(post.getCreated_at())
                .status(post.getStatus())
                .major(post.getUser().getUserInfo().getMajor())
                .build();

        return ApiResponse.success("data",postDTO);
    }
}