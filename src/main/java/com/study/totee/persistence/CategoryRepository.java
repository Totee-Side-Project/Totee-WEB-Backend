package com.study.totee.persistence;

import com.study.totee.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
    Optional<CategoryEntity> findByCategoryName(String name);
    boolean existsByCategoryName(String name);
    void deleteByCategoryName(String id);
}
