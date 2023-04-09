package com.sequitur.api.IdentityAccessManagement.domain.repository;

import com.sequitur.api.IdentityAccessManagement.domain.model.Student;
import com.sequitur.api.IdentityAccessManagement.domain.model.University;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Page<Student> findByUniversityId(Long universityId, Pageable pageable);
    Optional<Student> findByIdAndUniversityId(Long id, Long universityId);
}
