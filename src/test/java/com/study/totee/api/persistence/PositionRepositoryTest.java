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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.HashSet;

import static com.study.totee.api.util.EntityFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PositionRepositoryTest {

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void 포지션_등록(){
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
                .build();

        final PostEntity savedPostEntity = postRepository.save(postEntity);
        positionEntity.setPost(savedPostEntity);
        // when
        final PositionEntity savedPositionEntity = positionRepository.save(positionEntity);

        // then
        assertThat(positionRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @Order(2)
    @Rollback(value = false)
    public void 포지션_삭제(){

        // when
        positionRepository.deleteAllByPostId(1L);

        // then
        assertThat(positionRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Order(3)
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
