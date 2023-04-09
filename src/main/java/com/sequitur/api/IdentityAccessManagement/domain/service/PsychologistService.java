package com.sequitur.api.IdentityAccessManagement.domain.service;

import com.sequitur.api.IdentityAccessManagement.domain.model.Psychologist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PsychologistService {

    Page<Psychologist> getAllPsychologistsByUniversityId(Long universityId, Pageable pageable);

    Psychologist getPsychologistByIdAndUniversityId(Long universityId, Long psychologistId);

    ResponseEntity<?> deletePsychologist(Long universityId, Long psychologistId);

    Psychologist updatePsychologist(Long universityId, Long psychologistId, Psychologist psychologistRequest);

    Psychologist createPsychologist(Long universityId, Psychologist psychologist);

    Psychologist getPsychologistById(Long psychologistId);

    Page<Psychologist> getAllPsychologists(Pageable pageable);
}
