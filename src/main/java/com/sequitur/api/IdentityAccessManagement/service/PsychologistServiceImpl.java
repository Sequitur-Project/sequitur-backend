package com.sequitur.api.IdentityAccessManagement.service;

import com.sequitur.api.IdentityAccessManagement.domain.model.Psychologist;
import com.sequitur.api.IdentityAccessManagement.domain.model.University;
import com.sequitur.api.IdentityAccessManagement.domain.repository.PsychologistRepository;
import com.sequitur.api.IdentityAccessManagement.domain.repository.UniversityRepository;
import com.sequitur.api.IdentityAccessManagement.domain.service.PsychologistService;
import com.sequitur.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PsychologistServiceImpl implements PsychologistService {

    @Autowired
    private PsychologistRepository psychologistRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Override
    public Page<Psychologist> getAllPsychologistsByUniversityId(Long universityId, Pageable pageable) {
        return psychologistRepository.findByUniversityId(universityId, pageable);
    }

    @Override
    public Psychologist getPsychologistByIdAndUniversityId(Long universityId, Long psychologistId) {
        return psychologistRepository.findByIdAndUniversityId(psychologistId, universityId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Psychologist not found with Id " + psychologistId +
                                " and UniversityId " + psychologistId));
    }

    @Override
    public ResponseEntity<?> deletePsychologist(Long universityId, Long psychologistId) {
        Psychologist psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist", "Id", psychologistId));
        psychologistRepository.delete(psychologist);
        return ResponseEntity.ok().build();
    }

    @Override
    public Psychologist updatePsychologist(Long universityId, Long psychologistId, Psychologist psychologistRequest) {
        Psychologist psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist", "Id", psychologistId));
        psychologist.setFirstName(psychologistRequest.getFirstName());
        psychologist.setLastName(psychologistRequest.getLastName());
        psychologist.setEmail(psychologistRequest.getEmail());
        psychologist.setPassword(psychologistRequest.getPassword());
        psychologist.setTelephone(psychologistRequest.getTelephone());

        return psychologistRepository.save(psychologist);
    }

    @Override
    public Psychologist createPsychologist(Long universityId, Psychologist psychologist) {
        University university = universityRepository.findById(universityId).orElseThrow(() -> new ResourceNotFoundException("University", "Id", universityId));
        psychologist.setUniversity(university);
        return psychologistRepository.save(psychologist);
    }

    @Override
    public Psychologist getPsychologistById(Long psychologistId) {
        return psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist", "Id", psychologistId));
    }

    @Override
    public Page<Psychologist> getAllPsychologists(Pageable pageable) {
        return psychologistRepository.findAll(pageable);
    }
}
