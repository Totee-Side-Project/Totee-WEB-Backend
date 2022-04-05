package com.study.totee.persistence;

import com.study.totee.model.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, String> {
    Page<PostEntity> findAll(Pageable pageable);
}
