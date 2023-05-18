package com.sequitur.api.DiagnosticAndTreatment.domain.service;

import com.sequitur.api.DiagnosticAndTreatment.domain.model.UniversityDepressionIndicatorSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UniversityDepressionIndicatorSetService {


    UniversityDepressionIndicatorSet updateDepressionSet(Long universityId, Long depressionSetId, UniversityDepressionIndicatorSet universityDepressionIndicatorSetRequest);

    UniversityDepressionIndicatorSet getUniversityDepressionIndicatorSetsByUniversity(Long universityId);
}
