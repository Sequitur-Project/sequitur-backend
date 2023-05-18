package com.sequitur.api.DiagnosticAndTreatment.domain.repository;

import com.sequitur.api.DiagnosticAndTreatment.domain.model.UniversityDepressionIndicatorSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniversityDepressionIndicatorSetRepository extends JpaRepository<UniversityDepressionIndicatorSet, Long> {
    UniversityDepressionIndicatorSet findAllByUniversityId(Long universityId);
}
