package com.study.totee.api.persistence;

import com.study.totee.api.model.UserRefreshToken;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRefreshTokenRepositoryTest {

    @Autowired
    private UserRefreshTokenRepository userRefreshTokenRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void 리프레시토큰_등록() {
        // given
        final UserRefreshToken userRefreshToken = new UserRefreshToken();
        userRefreshToken.setUserId("TEST");
        userRefreshToken.setRefreshToken("TEST");

        // when
        final UserRefreshToken savedUserRefreshToken = userRefreshTokenRepository.save(userRefreshToken);

        // then
        assertThat(savedUserRefreshToken.getUserId()).isEqualTo("TEST");
        assertThat(savedUserRefreshToken.getRefreshToken()).isEqualTo("TEST");
    }

    @Test
    @Order(2)
    public void 리프레시토큰_조회() {
        // given
        final UserRefreshToken foundUserRefreshToken = userRefreshTokenRepository.findByUserId("TEST");

        // then
        assertThat(foundUserRefreshToken.getUserId()).isEqualTo("TEST");
        assertThat(foundUserRefreshToken.getRefreshToken()).isEqualTo("TEST");
    }

    @Test
    @Order(3)
    public void 리프레시토큰_아이디조회() {
        // given
        final UserRefreshToken foundUserRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken("TEST", "TEST");

        // then
        assertThat(foundUserRefreshToken.getUserId()).isEqualTo("TEST");
        assertThat(foundUserRefreshToken.getRefreshToken()).isEqualTo("TEST");
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void 리프레시토큰_삭제() {
        // given
        final UserRefreshToken foundUserRefreshToken = userRefreshTokenRepository.findByUserId("TEST");

        // when
        userRefreshTokenRepository.delete(foundUserRefreshToken);

        // then
        assertThat(userRefreshTokenRepository.findByUserId("TEST")).isNull();
        assertThat(userRefreshTokenRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    public void 초기화() {
        userRefreshTokenRepository.deleteAll();
    }
}
