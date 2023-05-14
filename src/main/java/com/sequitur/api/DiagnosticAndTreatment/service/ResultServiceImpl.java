package com.sequitur.api.DiagnosticAndTreatment.service;

import com.sequitur.api.DataCollection.domain.model.Conversation;
import com.sequitur.api.DataCollection.domain.repository.ConversationRepository;
import com.sequitur.api.DiagnosticAndTreatment.domain.model.Result;
import com.sequitur.api.DiagnosticAndTreatment.domain.repository.ResultRepository;
import com.sequitur.api.DiagnosticAndTreatment.domain.service.ResultService;
import com.sequitur.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Override
    public Result getResultByConversationId(Long conversationId) {
        return resultRepository.findByConversationId(conversationId);
    }

    @Override
    public Result getResultByIdAndConversationId(Long resultId, Long conversationId) {
        return resultRepository.findByIdAndConversationId(resultId, conversationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Result not found with Id " + resultId +
                                " and ConversationId " + conversationId));
    }

    @Override
    public ResponseEntity<?> deleteResult(Long resultId, Long conversationId) {
        Result result = resultRepository.findById(resultId)
                .orElseThrow(() -> new ResourceNotFoundException("Result", "Id", resultId));
        resultRepository.delete(result);
        return ResponseEntity.ok().build();
    }

    @Override
    public Result getResultById(Long resultId) {
        return resultRepository.findById(resultId)
                .orElseThrow(() -> new ResourceNotFoundException("Result", "Id", resultId));
    }

    @Override
    public Page<Result> getAllResults(Pageable pageable) {
        return resultRepository.findAll(pageable);
    }

    @Override
    public Result getResultByIdAndStudentId(Long resultId, Long studentId) {
        return resultRepository.findByIdAndStudentId(resultId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Result not found with Id " + resultId +
                                " and StudentId " + studentId));
    }

    @Override
    public List<Result> getAllResultsByStudentId(Long studentId) {
        return resultRepository.findByStudentId(studentId);
    }
}
