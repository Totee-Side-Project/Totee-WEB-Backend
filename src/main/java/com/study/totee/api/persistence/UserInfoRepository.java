package com.study.totee.api.persistence;

import com.study.totee.api.model.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, String> {
    UserInfoEntity findUserInfoEntityByUser_Id(String userId);
    Optional<UserInfoEntity> findByNickname(String nickname);
    boolean existsByNickname(String nickname);
}
