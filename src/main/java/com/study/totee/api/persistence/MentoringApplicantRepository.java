package com.study.totee.api.persistence;

import com.study.totee.api.model.Mentoring;
import com.study.totee.api.model.MentoringApplicant;
import com.study.totee.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentoringApplicantRepository extends JpaRepository<MentoringApplicant, Long> {
    boolean existsByUserAndMentoring_Id(User user, Long mentoringId);
    List<MentoringApplicant> findAllByMentoring(Mentoring mentoring);
    void deleteAllByMentoring(Mentoring mentoring);

    MentoringApplicant findByUserAndMentoring(User user, Mentoring mentoring);
}
