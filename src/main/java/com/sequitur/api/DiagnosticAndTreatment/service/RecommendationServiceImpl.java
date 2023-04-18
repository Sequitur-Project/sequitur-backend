package com.sequitur.api.DiagnosticAndTreatment.service;

import com.sequitur.api.DiagnosticAndTreatment.domain.model.Recommendation;
import com.sequitur.api.DiagnosticAndTreatment.domain.repository.RecommendationRepository;
import com.sequitur.api.DiagnosticAndTreatment.domain.service.RecommendationService;
import com.sequitur.api.IdentityAccessManagement.domain.model.Psychologist;
import com.sequitur.api.IdentityAccessManagement.domain.model.Student;
import com.sequitur.api.IdentityAccessManagement.domain.model.University;
import com.sequitur.api.IdentityAccessManagement.domain.repository.PsychologistRepository;
import com.sequitur.api.IdentityAccessManagement.domain.repository.StudentRepository;
import com.sequitur.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RecommendationServiceImpl implements RecommendationService {



    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private PsychologistRepository psychologistRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Recommendation getRecommendationByIdAndPsychologistIdAndStudentId(Long recommendationId, Long psychologistId, Long studentId) {
        return recommendationRepository.findByIdAndPsychologistIdAndStudentId(recommendationId,psychologistId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Recommendation not found with Id " + recommendationId +
                                " and StudentId " + studentId +
                        "and PsychologistId"+ psychologistId));
    }
    @Override
    public Page<Recommendation> getAllRecommendationsByStudentId(Long studentId, Pageable pageable) {
        return recommendationRepository.findAllByStudentId(studentId, pageable);
    }

    @Override
    public Recommendation getRecommendationByIdAndStudentId(Long recommendationId, Long studentId) {
        return recommendationRepository.findByIdAndStudentId(recommendationId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Recommendation not found with Id " + recommendationId +
                                " and StudentId " + studentId));
    }

    @Override
    public Page<Recommendation> getAllRecommendationsByPsychologistId(Long psychologistId, Pageable pageable) {
        return recommendationRepository.findAllByPsychologistId(psychologistId, pageable);
    }

    @Override
    public Recommendation getRecommendationByIdAndPsychologistId(Long recommendationId, Long psychologistId) {
        return recommendationRepository.findByIdAndPsychologistId(recommendationId, psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Recommendation not found with Id " + recommendationId +
                                " and PsychologistId " + psychologistId));
    }

    @Override
    public ResponseEntity<?> deleteRecommendation(Long recommendationId, Long psychologistId, Long studentId) {
        Recommendation recommendation = recommendationRepository.findByIdAndPsychologistIdAndStudentId(recommendationId,psychologistId,studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Recommendation", "Id", recommendationId));
        recommendationRepository.delete(recommendation);
        return ResponseEntity.ok().build();
    }

    @Override
    public Recommendation updateRecommendation(Long recommendationId, Long psychologistId, Long studentId, Recommendation recommendationRequest) {
        Recommendation recommendation = recommendationRepository.findByIdAndPsychologistIdAndStudentId(recommendationId,psychologistId,studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Recommendation", "Id", recommendationId));

        recommendation.setText(recommendationRequest.getText());
        return recommendationRepository.save(recommendation);
    }

    @Override
    public Recommendation createRecommendation(Long psychologistId, Long studentId, Recommendation recommendation) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        Psychologist psychologist = psychologistRepository.findById(psychologistId).orElseThrow(() -> new ResourceNotFoundException("Psychologist", "id", psychologistId));
        recommendation.setStudent(student);
        recommendation.setPsychologist(psychologist);
        return recommendationRepository.save(recommendation);
    }

    @Override
    public Page<Recommendation> getAllRecommendations(Pageable pageable) {
        return recommendationRepository.findAll(pageable);
    }
}
