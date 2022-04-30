package com.study.totee.service;

import com.study.totee.dto.CommentDTO;
import com.study.totee.model.CommentEntity;
import com.study.totee.model.PostEntity;
import com.study.totee.model.UserEntity;
import com.study.totee.persistence.CommentRepository;
import com.study.totee.persistence.PostRepository;
import com.study.totee.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostService postService;
    @Transactional
    public void save(CommentEntity commentEntity){
        PostEntity post = postService.findByPostId(commentEntity.getPost().getPostId());
        post.getComment().add(commentEntity);
        commentRepository.save(commentEntity);
    }

    @Transactional
    public void update(CommentDTO commentDTO, String userId){
        Optional<UserEntity> user = userRepository.findById(userId);
        CommentEntity commentEntity = commentRepository.findByCommentIdAndUser(commentDTO.getCommentId(), user.get());
        commentEntity.setContent(commentDTO.getContent());
    }

    @Transactional
    public void delete(Long commentId, String userId){
        Optional<UserEntity> user = userRepository.findById(userId);
        CommentEntity commentEntity = commentRepository.findByCommentIdAndUser(commentId, user.get());
        commentRepository.delete(commentEntity);
    }

    @Transactional(readOnly = true)
    public List<CommentEntity> CommentListByPostId(Long postId){
        List<CommentEntity> commentEntities = commentRepository.findCommentEntityByPost_PostId(postId);
        return commentEntities;
    }
}
