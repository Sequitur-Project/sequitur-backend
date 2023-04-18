package com.sequitur.api.DiagnosticAndTreatment.domain.service;

import com.sequitur.api.DiagnosticAndTreatment.domain.model.Recommendation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface RecommendationService {

    Page<Recommendation> getAllRecommendationsByStudentId(Long studentId, Pageable pageable);

    Recommendation getRecommendationByIdAndStudentId(Long recommendationId, Long studentId);

    Page<Recommendation> getAllRecommendationsByPsychologistId(Long psychologistId, Pageable pageable);

    Recommendation getRecommendationByIdAndPsychologistId(Long recommendationId, Long psychologistId);

    Recommendation getRecommendationByIdAndPsychologistIdAndStudentId(Long recommendationId, Long psychologistId,Long studentId);

    ResponseEntity<?> deleteRecommendation(Long recommendationId, Long psychologistId,Long studentId);

    Recommendation updateRecommendation(Long recommendationId, Long psychologistId, Long studentId, Recommendation recommendationRequest);

    Recommendation createRecommendation(Long psychologistId, Long studentId, Recommendation recommendation);

    Page<Recommendation> getAllRecommendations(Pageable pageable);
}
