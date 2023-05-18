package com.sequitur.api.DataCollection.domain.repository;

import com.sequitur.api.DataCollection.domain.model.Binnacle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BinnacleRepository extends JpaRepository<Binnacle, Long> {
    Optional<Binnacle> findByIdAndStudentId(Long id, Long studentId);
    Binnacle findByStudentId(Long studentId);
}
