package com.study.totee.api.persistence;

import com.study.totee.api.model.PositionEntity;
import com.study.totee.api.model.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<PositionEntity, Long> {

    void deleteAllByPost(PostEntity post);
}
