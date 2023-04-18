package com.sequitur.api.DataCollection.service;

import com.sequitur.api.DataCollection.domain.model.Binnacle;
import com.sequitur.api.DataCollection.domain.model.BinnacleEntry;
import com.sequitur.api.DataCollection.domain.model.Conversation;
import com.sequitur.api.DataCollection.domain.repository.BinnacleEntryRepository;
import com.sequitur.api.DataCollection.domain.repository.BinnacleRepository;
import com.sequitur.api.DataCollection.domain.service.BinnacleEntryService;
import com.sequitur.api.IdentityAccessManagement.domain.model.Psychologist;
import com.sequitur.api.IdentityAccessManagement.domain.model.Student;
import com.sequitur.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BinnacleEntryServiceImpl implements BinnacleEntryService {

    @Autowired
    private BinnacleRepository binnacleRepository;
    @Autowired
    private BinnacleEntryRepository binnacleEntryRepository;

    @Override
    public Page<BinnacleEntry> getAllBinnacleEntriesByBinnacleId(Long binnacleId, Pageable pageable) {
        return binnacleEntryRepository.findByBinnacleId(binnacleId, pageable);
    }

    @Override
    public BinnacleEntry getBinnacleEntryByIdAndBinnacleId(Long binnacleEntryId, Long binnacleId) {
        return binnacleEntryRepository.findByIdAndBinnacleId(binnacleEntryId, binnacleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "BinnacleEntry not found with Id " + binnacleEntryId +
                                " and BinnacleId " + binnacleId));
    }

    @Override
    public ResponseEntity<?> deleteBinnacleEntry(Long binnacleEntryId, Long binnacleId) {
        BinnacleEntry binnacleEntry = binnacleEntryRepository.findById(binnacleEntryId)
                .orElseThrow(() -> new ResourceNotFoundException("BinnacleEntry", "Id", binnacleEntryId));
        binnacleEntryRepository.delete(binnacleEntry);
        return ResponseEntity.ok().build();
    }

    @Override
    public BinnacleEntry updateBinnacleEntry(Long binnacleId, Long binnacleEntryId, BinnacleEntry binnacleEntryRequest) {
        BinnacleEntry binnacleEntry = binnacleEntryRepository.findById(binnacleEntryId)
                .orElseThrow(() -> new ResourceNotFoundException("BinnacleEntry", "Id", binnacleEntryId));
        binnacleEntry.setEmoji(binnacleEntryRequest.getEmoji());
        binnacleEntry.setFeeling(binnacleEntryRequest.getFeeling());
        binnacleEntry.setReason(binnacleEntryRequest.getReason());
        binnacleEntry.setExtraText(binnacleEntryRequest.getExtraText());

        return binnacleEntryRepository.save(binnacleEntry);
    }

    @Override
    public BinnacleEntry createBinnacleEntry(Long binnacleId, BinnacleEntry binnacleEntry) {
        Binnacle binnacle = binnacleRepository.findById(binnacleId).orElseThrow(() -> new ResourceNotFoundException("Binnacle", "Id", binnacleId));
        binnacleEntry.setBinnacle(binnacle);
        return binnacleEntryRepository.save(binnacleEntry);
    }

    @Override
    public Page<BinnacleEntry> getAllBinnacleEntries(Pageable pageable) {
        return binnacleEntryRepository.findAll(pageable);
    }
}
