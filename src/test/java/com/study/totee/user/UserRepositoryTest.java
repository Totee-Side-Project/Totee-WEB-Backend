package com.study.totee.api.persistence;

import com.study.totee.api.model.User;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import com.study.totee.type.ProviderType;
import com.study.totee.type.RoleType;
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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UserRepository 테스트 케이스
 *
 * @author Marine
 * @since 2022.07.31
 */

@DataJpaTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void 유저_등록() {
        // given
        LocalDateTime today = LocalDateTime.now();

        final User userEntity = User.builder()
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

        // when
        final User savedUserEntity = userRepository.save(userEntity);

        // then
        assertThat(savedUserEntity.getUserSeq()).isGreaterThan(0);
        assertThat(savedUserEntity.getId()).isEqualTo("TEST");
        assertThat(savedUserEntity.getUsername()).isEqualTo("TEST");
        assertThat(savedUserEntity.getEmail()).isEqualTo("NO_MAIL");
        assertThat(savedUserEntity.getEmailVerifiedYn()).isEqualTo("Y");
        assertThat(savedUserEntity.getProfileImageUrl()).isEqualTo("NO_IMAGE");
        assertThat(savedUserEntity.getProviderType()).isEqualTo(ProviderType.LOCAL);
        assertThat(savedUserEntity.getPassword()).isEqualTo("NO_PASSWORD");
        assertThat(savedUserEntity.getRoleType()).isEqualTo(RoleType.USER);
        assertThat(savedUserEntity.getCreatedAt()).isNotNull();
    }

    @Test
    @Order(2)
    public void 유저_조회() {
        // given

        // when
        final User foundUserEntity = userRepository.findById(1L).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        // then
        assertThat(foundUserEntity.getId()).isEqualTo("TEST");
        assertThat(userRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    public void 유저_수정(){
        // given
        User userEntity = userRepository.findById(1L).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));;

        // when
        userEntity.setId("TEST_UPDATE");

        // then
        assertThat(userRepository.findById("TEST_UPDATE")).isNotNull();
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void 유저_삭제(){
        // given
        User foundUserEntity = userRepository.findById(1L).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));;

        // when
        userRepository.delete(foundUserEntity);

        // then
        assertThat(userRepository.findById("TEST_UPDATE")).isNull();
        assertThat(userRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    public void 초기화(){
        userRepository.deleteAll();

        entityManager
                .createNativeQuery("ALTER TABLE tb_user ALTER COLUMN `user_seq` RESTART WITH 1")
                .executeUpdate();
    }
}
