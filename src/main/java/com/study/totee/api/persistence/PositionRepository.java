package com.study.totee.api.persistence;

import com.study.totee.api.model.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<PositionEntity, Long> {

}
