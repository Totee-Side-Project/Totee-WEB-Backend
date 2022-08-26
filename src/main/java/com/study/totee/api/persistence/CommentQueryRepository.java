package com.study.totee.api.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.totee.api.model.Comment;
import com.study.totee.api.model.QComment;
import com.study.totee.api.model.QPost;
import com.study.totee.api.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CommentQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Comment findByIdAndUser(Long id, User user) {
        return queryFactory.selectFrom(QComment.comment)
                .where(QComment.comment.id.eq(id)
                        .and(QComment.comment.user.eq(user)))
                .fetchOne();
    }

    public List<Comment> findAllByPost_Id(Long postId) {
        return queryFactory.selectFrom(QComment.comment)
                .leftJoin(QComment.comment.post, QPost.post).fetchJoin()
                .where(QComment.comment.post.id.eq(postId))
                .fetch();
    }
}
