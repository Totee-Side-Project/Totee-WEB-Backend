package com.study.totee.api.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.totee.api.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.study.totee.api.model.QCategory.category;

@RequiredArgsConstructor
@Repository
public class CategoryQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Category findByCategoryName(String name) {
        return queryFactory.selectFrom(category)
                .where(category.categoryName.eq(name))
                .fetchOne();
    }

    public List<Category> findAll() {
        return queryFactory.selectFrom(category)
                .fetch();
    }
}
