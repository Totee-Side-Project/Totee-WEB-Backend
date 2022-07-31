package com.study.totee.api.persistence;

import com.study.totee.api.model.PostEntity;
import com.study.totee.api.model.UserEntity;
import com.study.totee.type.PositionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<PostEntity, String> {
    PostEntity findByPostId(Long postId);
    PostEntity findByPostIdAndUser(Long postId, UserEntity user);
    Page<PostEntity> findAll(Pageable pageable);
    Page<PostEntity> findAllByTitleContaining(String keyword, Pageable pageable);

    @Query(value = "SELECT p FROM PostEntity p JOIN PositionEntity p2 ON p.postId = p2.post.postId WHERE p2.position = ?1 and p.category.categoryName = '프로젝트' and p.status = 'Y'")
    Page<PostEntity> findAllByPosition(PositionType position, Pageable pageable);
}
