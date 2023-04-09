package com.sequitur.api.IdentityAccessManagement.domain.repository;


import com.sequitur.api.IdentityAccessManagement.domain.model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    Optional<University> findByRuc(String ruc);
}
