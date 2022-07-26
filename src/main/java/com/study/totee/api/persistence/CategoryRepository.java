package com.study.totee.api.persistence;

import com.study.totee.api.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    CategoryEntity findByCategoryName(String name);
    boolean existsByCategoryName(String name);
    void deleteByCategoryName(String id);
}
