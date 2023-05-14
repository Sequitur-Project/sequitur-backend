package com.sequitur.api.DiagnosticAndTreatment.domain.service;

import com.sequitur.api.DataCollection.domain.model.Conversation;
import com.sequitur.api.DiagnosticAndTreatment.domain.model.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ResultService {

    Result getResultByConversationId(Long conversationId);
    Result getResultByIdAndConversationId(Long resultId, Long conversationId);

    Result getResultByIdAndStudentId(Long resultId, Long studentId);

    List<Result> getAllResultsByStudentId(Long studentId);

    ResponseEntity<?> deleteResult(Long resultId, Long conversationId);

    Result getResultById(Long resultId);

    Page<Result> getAllResults(Pageable pageable);
}
