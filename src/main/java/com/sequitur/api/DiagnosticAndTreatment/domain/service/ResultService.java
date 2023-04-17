package com.sequitur.api.DiagnosticAndTreatment.domain.service;

import com.sequitur.api.DataCollection.domain.model.Conversation;
import com.sequitur.api.DiagnosticAndTreatment.domain.model.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ResultService {


    Result getResultByIdAndConversationId(Long resultId, Long conversationId);

    ResponseEntity<?> deleteResult(Long resultId, Long conversationId);

    Result getResultById(Long resultId);

    Page<Result> getAllResults(Pageable pageable);
}
