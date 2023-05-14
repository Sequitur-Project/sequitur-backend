package com.sequitur.api.DiagnosticAndTreatment.controller;

import com.sequitur.api.DataCollection.domain.model.Conversation;
import com.sequitur.api.DataCollection.resource.ConversationResource;
import com.sequitur.api.DiagnosticAndTreatment.domain.model.Result;
import com.sequitur.api.DiagnosticAndTreatment.domain.service.ResultService;
import com.sequitur.api.DiagnosticAndTreatment.resource.ResultResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "results", description = "Results API")
@RestController
@RequestMapping("/api")
public class ResultController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ResultService resultService;


    @GetMapping("/conversations/{conversationId}/results")
    public ResultResource getResultByConversationId(@PathVariable(name = "conversationId") Long conversationId) {
        return convertToResource(resultService.getResultByConversationId(conversationId));
    }

    @GetMapping("/conversations/{conversationId}/results/{resultId}")
    public ResultResource getResultByIdAndConversationId(@PathVariable(name = "resultId") Long resultId,
                                                          @PathVariable(name = "conversationId") Long conversationId) {
        return convertToResource(resultService.getResultByIdAndConversationId(resultId, conversationId));
    }

    @GetMapping("/students/{studentId}/results")
    public List<ResultResource> getAllResultsByStudentId(
            @PathVariable(name = "studentId") Long studentId) {
        List<Result> results =resultService.getAllResultsByStudentId(studentId);
        List<ResultResource> resources = results.stream().map(this::convertToResource).collect(Collectors.toList());
        return resources;
    }

    @GetMapping("/students/{studentId}/results/{resultId}")
    public ResultResource getResultByIdAndStudentId(@PathVariable(name = "studentId") Long studentId,
                                                                @PathVariable(name = "resultId") Long resultId) {
        return convertToResource(resultService.getResultByIdAndStudentId(resultId, studentId));
    }



    @DeleteMapping("/conversations/{conversationId}/results/{resultId}")
    public ResponseEntity<?> deleteResult(@PathVariable(name = "resultId") Long resultId,
                                                @PathVariable(name = "conversationId") Long conversationId) {
        return resultService.deleteResult(resultId, conversationId);
    }

    @Operation(summary = "Get Results", description = "Get All Results by Pages", tags = { "results" })
    @GetMapping("/results")
    public Page<ResultResource> getAllResults(
            @Parameter(description="Pageable Parameter")
            Pageable pageable) {
        Page<Result> resultsPage = resultService.getAllResults(pageable);
        List<ResultResource> resources = resultsPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());

        return new PageImpl<ResultResource>(resources,pageable , resources.size());
    }

    private ResultResource convertToResource(Result entity) {
        return mapper.map(entity, ResultResource.class);
    }
}
