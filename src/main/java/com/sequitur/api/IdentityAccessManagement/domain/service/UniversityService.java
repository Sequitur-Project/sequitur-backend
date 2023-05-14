package com.sequitur.api.IdentityAccessManagement.domain.service;

import com.sequitur.api.IdentityAccessManagement.domain.model.University;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UniversityService {
    ResponseEntity<?> deleteUniversity(Long universityId);

    University updateUniversity(Long universityId, University universityRequest);

    University createUniversity(University university);

    University getUniversityById(Long universityId);

    List<University> getAllUniversities();

    University getUniversityByRuc(String ruc);
}
