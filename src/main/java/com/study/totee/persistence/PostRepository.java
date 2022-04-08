package com.study.totee.persistence;

import com.study.totee.model.CategoryEntity;
import com.study.totee.model.PostEntity;
import com.study.totee.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, String> {
    Page<PostEntity> findAll(Pageable pageable);
    PostEntity findByPostId(Long postId);
    PostEntity findByPostIdAndUser(Long postId, UserEntity user);
    Page<PostEntity> findAllByCategory_CategoryName(String categoryName, Pageable pageable);
}
