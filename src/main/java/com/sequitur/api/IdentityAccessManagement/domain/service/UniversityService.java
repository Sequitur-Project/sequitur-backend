package com.sequitur.api.IdentityAccessManagement.domain.service;

import com.sequitur.api.IdentityAccessManagement.domain.model.University;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface UniversityService {
    ResponseEntity<?> deleteUniversity(Long universityId);

    University updateUniversity(Long universityId, University universityRequest);

    University createUniversity(University university);

    University getUniversityById(Long universityId);

    Page<University> getAllUniversities(Pageable pageable);

    University getUniversityByRuc(String ruc);
}
