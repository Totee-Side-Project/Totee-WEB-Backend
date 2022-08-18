package com.study.totee.api.persistence;

import com.study.totee.api.model.UserInfo;
import com.study.totee.exption.BadRequestException;
import com.study.totee.exption.ErrorCode;
import com.study.totee.type.PositionType;
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


@DataJpaTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserInfoRepositoryTest {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void 유저정보_등록() {
        // given
        final UserInfo userInfoEntity = UserInfo.builder()
                .nickname("TEST")
                .positionList(null)
                .position(PositionType.BACK_END)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        // when
        final UserInfo savedUserInfoEntity = userInfoRepository.save(userInfoEntity);

        // then
        assertThat(savedUserInfoEntity.getId()).isNotNull();
        assertThat(savedUserInfoEntity.getNickname()).isEqualTo("TEST");
        assertThat(savedUserInfoEntity.getCreatedAt()).isNotNull();
        assertThat(savedUserInfoEntity.getModifiedAt()).isNotNull();
        assertThat(savedUserInfoEntity.getPosition()).isEqualTo(PositionType.BACK_END);
    }

    @Test
    @Order(2)
    public void 유저정보_조회() {
        // given

        // when
        final UserInfo foundUserInfoEntity = userInfoRepository.findById(1L).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        // then
        assertThat(foundUserInfoEntity).isNotNull();
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    public void 유저정보_수정() {
        // given
        UserInfo foundUserInfoEntity = userInfoRepository.findById(1L).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        // when
        foundUserInfoEntity.setNickname("nTest");

        // then
        assertThat(foundUserInfoEntity.getNickname()).isEqualTo("nTest");
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void 유저정보_삭제() {
        // given
        UserInfo foundUserInfoEntity = userInfoRepository.findById(1L).orElseThrow(
                ()-> new BadRequestException(ErrorCode.NOT_EXIST_USER_ERROR));

        // when
        userInfoRepository.delete(foundUserInfoEntity);

        // then
        assertThat(userInfoRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    public void 초기화() {
        this.userInfoRepository.deleteAll();

        this.entityManager
                .createNativeQuery("ALTER TABLE tb_user_info ALTER COLUMN `user_info_id` RESTART WITH 1")
                .executeUpdate();
    }

}
