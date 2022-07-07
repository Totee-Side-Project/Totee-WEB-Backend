package com.study.totee.api.persistence;

import com.study.totee.api.model.PositionEntity;
import com.study.totee.api.model.PostEntity;
import com.study.totee.type.PositionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<PositionEntity, Long> {

    void deleteAllByPost(PostEntity post);
}
