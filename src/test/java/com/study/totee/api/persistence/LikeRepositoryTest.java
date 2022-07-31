package com.study.totee.api.persistence;

import com.study.totee.api.model.*;
import com.study.totee.type.PeriodType;
import com.study.totee.type.PositionType;
import com.study.totee.type.ProviderType;
import com.study.totee.type.RoleType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static com.study.totee.api.util.EntityFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LikeRepositoryTest {

    @Autowired
    private LikeRepository likeRepository;

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
    public void 좋아요_등록(){
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

        final LikeEntity likeEntity = LikeEntity.builder()
                .likeId(1L)
                .post(postEntity)
                .user(userEntity)
                .build();

        // when
        likeRepository.save(likeEntity);
        List<LikeEntity> likeEntityList = likeRepository.findAll();

        // then
        assertThat(likeEntityList.size()).isEqualTo(1);
    }

    @Test
    @Order(2)
    public void 좋아요_해당포스트에좋아요했는지체크함() {
        // given
        final UserEntity userEntity = userRepository.findById("TEST");
        final PostEntity postEntity = postRepository.findByPostId(1L);

        // when
        final Boolean likeCheck = likeRepository.existsByUser_IdAndPost_PostId(userEntity
                .getId(), postEntity.getPostId());

        // then
        assertThat(likeCheck).isTrue();
    }

    @Test
    @Order(3)
    public void 좋아요_내가좋아요누른글조회(){
        // given
        Pageable pageable = PageRequest.of(0, 5);
        final UserEntity userEntity = userRepository.findById("TEST");

        // when
        final Page<PostEntity> postEntityList = likeRepository.findAllByLikedPost(userEntity, pageable);

        // then
        assertThat(postEntityList.getTotalElements()).isEqualTo(1);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void 초기화() {
        this.postRepository.deleteAll();
        this.likeRepository.deleteAll();
        this.userRepository.deleteAll();
        this.categoryRepository.deleteAll();

        this.entityManager
                .createNativeQuery("ALTER TABLE tb_post ALTER COLUMN `post_id` RESTART WITH 1")
                .executeUpdate();
        this.entityManager
                .createNativeQuery("ALTER TABLE tb_like ALTER COLUMN `like_id` RESTART WITH 1")
                .executeUpdate();
        this.entityManager
                .createNativeQuery("ALTER TABLE tb_user ALTER COLUMN `user_seq` RESTART WITH 1")
                .executeUpdate();
        this.entityManager
                .createNativeQuery("ALTER TABLE tb_category ALTER COLUMN `category_id` RESTART WITH 1")
                .executeUpdate();
    }


}
