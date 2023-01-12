package com.study.totee.api.persistence;

import com.study.totee.api.model.MentoringApplicant;
import com.study.totee.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentoringApplicantRepository extends JpaRepository<MentoringApplicant, Long> {
    boolean existsByUserAndMentoring_Id(User user, Long mentoringId);

}
