package com.sequitur.api.ProactiveCommunication.domain.repository;

import com.sequitur.api.ProactiveCommunication.domain.model.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportRepository extends JpaRepository<Support, Long> {
}
