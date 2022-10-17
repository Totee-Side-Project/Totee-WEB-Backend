package com.study.totee.api.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.totee.api.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ApplicantQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Applicant findByUserAndPost(User user, Post post) {
        return queryFactory.selectFrom(QApplicant.applicant)
                .where(QApplicant.applicant.post.eq(post)
                        .and(QApplicant.applicant.user.eq(user)))
                .fetchOne();
    }

    public boolean existsByUserIdAndPostId(Long userId, Long postId) {
        Integer fetchOne = queryFactory.selectOne()
                .from(QApplicant.applicant)
                .where(QApplicant.applicant.user.userSeq.eq(userId)
                        .and(QApplicant.applicant.post.id.eq(postId)))
                .fetchFirst();

        return fetchOne != null;
    }
}
