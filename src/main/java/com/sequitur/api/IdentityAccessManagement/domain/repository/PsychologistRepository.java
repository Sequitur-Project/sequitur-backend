package com.sequitur.api.IdentityAccessManagement.domain.repository;

import com.sequitur.api.IdentityAccessManagement.domain.model.Psychologist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PsychologistRepository extends JpaRepository<Psychologist, Long> {

    Page<Psychologist> findByUniversityId(Long universityId, Pageable pageable);

    Optional<Psychologist> findByIdAndUniversityId(Long id, Long universityId);
}
