package com.sequitur.api.DiagnosticAndTreatment.domain.repository;

import com.sequitur.api.DiagnosticAndTreatment.domain.model.Recommendation;
import com.sequitur.api.IdentityAccessManagement.domain.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation,Long> {

    Optional<Recommendation> findByIdAndStudentId(Long id, Long studentId);
    Optional<Recommendation> findByIdAndPsychologistId(Long id, Long psychologistId);
    Page<Recommendation> findAllByPsychologistId(Long psychologistId, Pageable pageable);
    Page<Recommendation> findAllByStudentId(Long studentId, Pageable pageable);

    Optional<Recommendation> findByIdAndPsychologistIdAndStudentId(Long id, Long psychologistId,Long studentId);
}
