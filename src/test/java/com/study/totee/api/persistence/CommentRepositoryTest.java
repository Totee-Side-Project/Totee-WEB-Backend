package com.study.totee.api.persistence;

import com.study.totee.api.model.*;
import com.study.totee.type.PeriodType;
import com.study.totee.type.ProviderType;
import com.study.totee.type.RoleType;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.study.totee.api.util.EntityFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void 댓글_등록() {
        // given
        LocalDateTime today = LocalDateTime.now();

        final CategoryEntity categoryEntity = CategoryEntity.builder()
                .categoryName("프로젝트")
                .build();
        categoryRepository.save(categoryEntity);

        final UserEntity userEntity = UserEntity.builder()
                .id("TEST")
                .username("TEST")
                .email("NO_MAIL")
                .emailVerifiedYn("Y")
                .profileImageUrl("NO_IMAGE")
                .providerType(ProviderType.LOCAL)
                .password("NO_PASSWORD")
                .roleType(RoleType.USER)
                .createdAt(today)
                .modifiedAt(today)
                .build();
        userRepository.save(userEntity);

        final PostEntity postEntity = PostEntity.builder()
                .postId(1L)
                .title("TEST")
                .content("TEST")
                .user(user())
                .period(PeriodType.ShortTerm)
                .onlineOrOffline("ONLINE")
                .status("Y")
                .commentNum(0)
                .view(0)
                .likeNum(0)
                .recruitNum("1~2명")
                .contactMethod("노션")
                .contactLink("https://www.naver.com")
                .createdAt(today)
                .modifiedAt(today)
                .user(userEntity)
                .category(categoryEntity)
                .build();
        postRepository.save(postEntity);

        final CommentEntity commentEntity = CommentEntity.builder()
                .commentId(1L)
                .profileImageUrl(userEntity.getProfileImageUrl())
                .post(postEntity)
                .user(userEntity)
                .content("TEST")
                .build();
        // when
        final CommentEntity savedCommentEntity = commentRepository.save(commentEntity);

        // then
        assertThat(savedCommentEntity.getCommentId()).isGreaterThan(0);
        assertThat(savedCommentEntity.getContent()).isEqualTo("TEST");
        assertThat(savedCommentEntity.getProfileImageUrl()).isEqualTo(userEntity.getProfileImageUrl());
        assertThat(savedCommentEntity.getPost().getPostId()).isEqualTo(postEntity.getPostId());
        assertThat(savedCommentEntity.getUser().getId()).isEqualTo(userEntity.getId());
    }

    @Test
    @Order(2)
    public void 댓글_조회() {
        // given

        // when
        final CommentEntity foundCommentEntity = commentRepository.findByCommentId(1L);

        // then
        assertThat(foundCommentEntity.getCommentId()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    public void 댓글_내댓글조회() {
        // given
        final UserEntity userEntity = userRepository.findById("TEST");

        // when
        final CommentEntity foundCommentEntity = commentRepository.findByCommentIdAndUser(1L, userEntity);

        // then
        assertThat(foundCommentEntity.getCommentId()).isEqualTo(1L);
    }

    @Test
    @Order(4)
    public void 댓글_포스트아이디로조회() {
        // given
        final PostEntity postEntity = postRepository.findByPostId(1L);

        // when
        final List<CommentEntity> foundCommentEntity = commentRepository.findCommentEntityByPost_PostId(postEntity.getPostId());

        // then
        assertThat(foundCommentEntity.size()).isEqualTo(1);
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    public void 댓글_수정(){
        // given
        final CommentEntity commentEntity = commentRepository.findByCommentId(1L);

        // when
        commentEntity.setContent("TEST_UPDATE");
        final CommentEntity updatedCommentEntity = commentRepository.findByCommentId(1L);
        // then
        assertThat(updatedCommentEntity.getContent()).isEqualTo("TEST_UPDATE");
    }

    @Test
    @Order(6)
    @Rollback(value = false)
    public void 댓글_삭제(){
        // given
        final CommentEntity commentEntity = commentRepository.findByCommentId(1L);
        // when
        commentRepository.delete(commentEntity);
        final CommentEntity deletedCommentEntity = commentRepository.findByCommentId(1L);
        // then
        assertThat(deletedCommentEntity).isNull();
    }

    @Test
    @Order(7)
    @Rollback(value = false)
    public void 초기화(){
        this.commentRepository.deleteAll();
        this.postRepository.deleteAll();
        this.userRepository.deleteAll();
        this.categoryRepository.deleteAll();

        this.entityManager
                .createNativeQuery("ALTER TABLE tb_post ALTER COLUMN `post_id` RESTART WITH 1")
                .executeUpdate();
        this.entityManager
                .createNativeQuery("ALTER TABLE tb_comment ALTER COLUMN `comment_id` RESTART WITH 1")
                .executeUpdate();
        this.entityManager
                .createNativeQuery("ALTER TABLE tb_user ALTER COLUMN `user_seq` RESTART WITH 1")
                .executeUpdate();
        this.entityManager
                .createNativeQuery("ALTER TABLE tb_category ALTER COLUMN `category_id` RESTART WITH 1")
                .executeUpdate();
    }
}
