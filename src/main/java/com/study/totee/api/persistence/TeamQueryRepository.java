package com.study.totee.api.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.totee.api.model.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.study.totee.api.model.QPost.post;
import static com.study.totee.api.model.QTeam.team;
import static com.study.totee.api.model.QUser.user;

@RequiredArgsConstructor
@Repository
public class TeamQueryRepository {

    private final JPAQueryFactory queryFactory;


    public boolean existsByPostIdAndUserId(Long postId, String userId) {
        Integer fetchOne = queryFactory.selectOne()
                .from(team)
                .where(team.user.id.eq(userId)
                        .and(team.post.id.eq(postId)))
                .fetchFirst();

        return fetchOne != null;
    }

    public boolean existsByMentoringIdAndUserId(Long mentoringId, String userId) {
        Integer fetchOne = queryFactory.selectOne()
                .from(team)
                .where(team.user.id.eq(userId)
                        .and(team.mentoring.id.eq(mentoringId)))
                .fetchFirst();

        return fetchOne != null;
    }

    public List<Team> findAllByPostId(Long postId) {
        return queryFactory.selectFrom(team)
                .where(team.post.id.eq(postId))
                .leftJoin(team.user, user).fetchJoin()
                .leftJoin(team.post, post).fetchJoin()
                .distinct()
                .fetch();
    }
}
