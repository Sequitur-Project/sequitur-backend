package com.sequitur.api.DataCollection.domain.repository;

import com.sequitur.api.DataCollection.domain.model.BinnacleEntry;
import com.sequitur.api.DataCollection.domain.model.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BinnacleEntryRepository extends JpaRepository<BinnacleEntry,Long> {

    Page<BinnacleEntry> findByBinnacleId(Long binnacleId, Pageable pageable);
    Optional<BinnacleEntry> findByIdAndBinnacleId(Long id, Long binnacleId);
}
