package com.study.totee.api.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.totee.api.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.study.totee.api.model.QUser.user;
import static com.study.totee.api.model.QUserInfo.userInfo;

@RequiredArgsConstructor
@Repository
public class UserQueryRepository {
    private final JPAQueryFactory queryFactory;

    public User findById(String id) {
        return queryFactory.selectFrom(user)
                .where(user.id.eq(id))
                .fetchOne();
    }

    public User findByUserInfo_Nickname(String nickname){
        return queryFactory.selectFrom(user)
                .innerJoin(user.userInfo, userInfo)
                .fetchJoin()
                .where(userInfo.nickname.eq(nickname))
                .fetchOne();
    }
}
