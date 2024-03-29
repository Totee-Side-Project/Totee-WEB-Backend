package com.study.totee.api.persistence;

import com.study.totee.api.model.Post;
import com.study.totee.api.model.User;
import com.study.totee.type.PositionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByIdAndUser(Long postId, User user);
    Page<Post> findAll(Pageable pageable);
    Page<Post> findAllByTitleContaining(String keyword, Pageable pageable);

    @Query(value = "SELECT p FROM Post p JOIN Position p2 ON p.id = p2.post.id WHERE p2.position = ?1 and p.status = 'Y'")
    Page<Post> findAllByPosition(PositionType position, Pageable pageable);

    @Query(value = "SELECT p FROM Post p LEFT JOIN Like l " + "ON l.post.id = p.id WHERE l.user = ?1")
    Page<Post> findAllByLikedPost(User user, Pageable pageable);

    Page<Post> findAllByUser_Id(Pageable pageable, String userId);

    @Query(value = "SELECT p FROM Post p WHERE p.id IN (SELECT t.post.id FROM Team t WHERE t.user = ?1)")
    Page<Post> findAllByMyStudyTeam(User user, Pageable pageable);
}
