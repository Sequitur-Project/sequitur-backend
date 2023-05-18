package com.sequitur.api.DataCollection.domain.service;

import com.sequitur.api.DataCollection.domain.model.Binnacle;
import com.sequitur.api.DataCollection.domain.model.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface BinnacleService {
    ResponseEntity<?> deleteBinnacle(Long binnacleId, Long studentId);

    Binnacle createBinnacle(Long studentId, Binnacle binnacle);

    Page<Binnacle> getAllBinnacles(Pageable pageable);

    Binnacle getBinnacleByIdAndStudentId(Long binnacleId, Long studentId);

    Binnacle getByStudentId(Long studentId);
}
