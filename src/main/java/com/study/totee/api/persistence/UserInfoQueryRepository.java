package com.study.totee.api.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.totee.api.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.study.totee.api.model.QUserInfo.userInfo;

@RequiredArgsConstructor
@Repository
public class UserInfoQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Boolean existsByNickname(String nickname) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(userInfo)
                .where(userInfo.nickname.eq(nickname))
                .fetchFirst();

        return fetchOne != null;
    }

    public UserInfo findByUserId(String userId) {
        return queryFactory
                .selectFrom(userInfo)
                .where(userInfo.user.id.eq(userId))
                .fetchOne();
    }
}
