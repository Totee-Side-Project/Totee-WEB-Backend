package com.study.totee.api.persistence;

import com.study.totee.api.model.CategoryEntity;
import com.study.totee.api.model.PositionEntity;
import com.study.totee.api.model.PostEntity;
import com.study.totee.api.model.UserEntity;
import com.study.totee.type.PeriodType;
import com.study.totee.type.PositionType;
import com.study.totee.type.ProviderType;
import com.study.totee.type.RoleType;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.study.totee.api.util.EntityFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void 게시글_등록() {
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

        PositionEntity positionEntity = new PositionEntity();
        positionEntity.setPosition(PositionType.ANDROID);

        HashSet<PositionEntity> positionEntityList = new HashSet<>();
        positionEntityList.add(positionEntity);

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
                .positionList(positionEntityList)
                .build();


        // when
        final PostEntity savedPostEntity = postRepository.save(postEntity);
        positionEntity.setPost(savedPostEntity);
        positionRepository.save(positionEntity);
        savedPostEntity.setPositionList(positionEntityList);

        // then

        assertThat(savedPostEntity.getPostId()).isNotNull();
        assertThat(savedPostEntity.getTitle()).isEqualTo("TEST");
        assertThat(savedPostEntity.getContent()).isEqualTo("TEST");
        assertThat(savedPostEntity.getUser().getUsername()).isEqualTo("TEST");
        assertThat(savedPostEntity.getCategory().getCategoryName()).isEqualTo("프로젝트");
        assertThat(savedPostEntity.getCreatedAt()).isNotNull();
        assertThat(savedPostEntity.getModifiedAt()).isNotNull();
        assertThat(savedPostEntity.getStatus()).isEqualTo("Y");
        assertThat(savedPostEntity.getCommentNum()).isEqualTo(0);
        assertThat(savedPostEntity.getView()).isEqualTo(0);
        assertThat(savedPostEntity.getLikeNum()).isEqualTo(0);
        assertThat(savedPostEntity.getRecruitNum()).isEqualTo("1~2명");
        assertThat(savedPostEntity.getContactMethod()).isEqualTo("노션");
        assertThat(savedPostEntity.getContactLink()).isEqualTo("https://www.naver.com");
        assertThat(savedPostEntity.getPeriod()).isEqualTo(PeriodType.ShortTerm);
        assertThat(savedPostEntity.getPositionList()).isNotNull();
    }

    @Test
    @Order(2)
    public void 게시글_아이디조회() {
        // given

        // when
        final PostEntity postEntity = postRepository.findByPostId(1L);

        // then
        assertThat(postEntity.getPostId()).isEqualTo(1L);
        assertThat(postRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @Order(3)
    public void 게시글_본인게시글아이디조회() {
        // given
        final UserEntity userEntity = userRepository.findById("TEST");
        // when
        final PostEntity postEntity = postRepository.findByPostIdAndUser(1L, userEntity);

        // then
        assertThat(postEntity.getPostId()).isEqualTo(1L);
        assertThat(postRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @Order(4)
    public void 게시글_제목검색_페이징() {
        // given
        Pageable pageable = PageRequest.of(0, 5);
        final String title = "TEST";
        // when
        final Page<PostEntity> postEntities = postRepository.findAllByTitleContaining(title, pageable);
        // then
        assertThat(postEntities.getTotalElements()).isEqualTo(1);
    }

    @Test
    @Order(5)
    public void 게시글_전체조회_페이징() {
        // given
        Pageable pageable = PageRequest.of(0, 5);
        // when
        final Page<PostEntity> postEntities = postRepository.findAll(pageable);

        // then
        assertThat(postEntities.getTotalElements()).isEqualTo(1);
    }

    @Test
    @Order(6)
    public void 게시글_프로젝트모집분야와일치하는게시글조회_페이징(){
        // given
        Pageable pageable = PageRequest.of(0, 5);

        // when
        final Page<PostEntity> postEntities = postRepository.findAllByPosition(PositionType.ANDROID, pageable);

        // then
        assertThat(postEntities.getTotalElements()).isEqualTo(1);
    }

    @Test
    @Order(7)
    @Rollback(value = false)
    public void 포스트_수정(){
        // given
        final PostEntity postEntity = postRepository.findByPostId(1L);

        // when
        postEntity.setTitle("TEST2");
        postEntity.setContent("TEST2");
        final PostEntity updatedPostEntity = postRepository.findByPostId(1L);

        // then
        assertThat(updatedPostEntity.getTitle()).isEqualTo("TEST2");
        assertThat(updatedPostEntity.getContent()).isEqualTo("TEST2");
    }

    @Test
    @Order(8)
    @Rollback(value = false)
    public void 포스트_삭제(){
        // given
        final PostEntity postEntity = postRepository.findByPostId(1L);

        // when
        postRepository.delete(postEntity);
        final PostEntity deletedPostEntity = postRepository.findByPostId(1L);

        // then
        assertThat(deletedPostEntity).isNull();
    }

    @Test
    @Order(9)
    @Rollback(value = false)
    public void 초기화() {
        this.postRepository.deleteAll();
        this.positionRepository.deleteAll();
        this.userRepository.deleteAll();
        this.categoryRepository.deleteAll();

        this.entityManager
                .createNativeQuery("ALTER TABLE tb_post ALTER COLUMN `post_id` RESTART WITH 1")
                .executeUpdate();
        this.entityManager
                .createNativeQuery("ALTER TABLE tb_position ALTER COLUMN `position_id` RESTART WITH 1")
                .executeUpdate();
        this.entityManager
                .createNativeQuery("ALTER TABLE tb_user ALTER COLUMN `user_seq` RESTART WITH 1")
                .executeUpdate();
        this.entityManager
                .createNativeQuery("ALTER TABLE tb_category ALTER COLUMN `category_id` RESTART WITH 1")
                .executeUpdate();
    }
}
