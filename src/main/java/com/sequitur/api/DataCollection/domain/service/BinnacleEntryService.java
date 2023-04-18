package com.sequitur.api.DataCollection.domain.service;

import com.sequitur.api.DataCollection.domain.model.BinnacleEntry;
import com.sequitur.api.DataCollection.domain.model.Conversation;
import com.sequitur.api.IdentityAccessManagement.domain.model.Psychologist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface BinnacleEntryService {

    Page<BinnacleEntry> getAllBinnacleEntriesByBinnacleId(Long binnacleId, Pageable pageable);

    BinnacleEntry getBinnacleEntryByIdAndBinnacleId(Long binnacleEntryId, Long binnacleId);

    ResponseEntity<?> deleteBinnacleEntry(Long binnacleEntryId, Long binnacleId);

    BinnacleEntry updateBinnacleEntry(Long binnacleId, Long binnacleEntryId, BinnacleEntry binnacleEntryRequest);

    BinnacleEntry createBinnacleEntry(Long binnacleId, BinnacleEntry binnacleEntry);

    Page<BinnacleEntry> getAllBinnacleEntries(Pageable pageable);
}
