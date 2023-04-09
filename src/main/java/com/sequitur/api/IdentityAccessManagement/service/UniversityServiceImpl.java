package com.sequitur.api.IdentityAccessManagement.service;

import com.sequitur.api.IdentityAccessManagement.domain.model.Manager;
import com.sequitur.api.IdentityAccessManagement.domain.model.University;
import com.sequitur.api.IdentityAccessManagement.domain.repository.ManagerRepository;
import com.sequitur.api.IdentityAccessManagement.domain.repository.UniversityRepository;
import com.sequitur.api.IdentityAccessManagement.domain.service.UniversityService;
import com.sequitur.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UniversityServiceImpl implements UniversityService {
    @Autowired
    private UniversityRepository universityRepository;

    @Override
    public ResponseEntity<?> deleteUniversity(Long universityId) {
        University university = universityRepository.findById(universityId).orElseThrow(() -> new ResourceNotFoundException("University", "Id", universityId));
        universityRepository.delete(university);
        return ResponseEntity.ok().build();
    }

    @Override
    public University updateUniversity(Long universityId, University universityRequest) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ResourceNotFoundException("University", "Id", universityId));
        university.setName(universityRequest.getName());
        university.setCountry(universityRequest.getCountry());
        university.setCity(universityRequest.getCity());
        university.setAddress(universityRequest.getAddress());
        university.setZipCode(universityRequest.getZipCode());

        return universityRepository.save(university);
    }

    @Override
    public University createUniversity(University university) {
        return universityRepository.save(university);
    }

    @Override
    public University getUniversityById(Long universityId) {
        University university = universityRepository.findById(universityId).orElseThrow(() -> new ResourceNotFoundException("University", "Id", universityId));
        return university;
    }

    @Override
    public Page<University> getAllUniversities(Pageable pageable) {
        return universityRepository.findAll(pageable);
    }

    @Override
    public University getUniversityByRuc(String ruc) {
        return universityRepository.findByRuc(ruc).orElseThrow(() -> new ResourceNotFoundException("University", "Ruc", ruc));
    }
}
