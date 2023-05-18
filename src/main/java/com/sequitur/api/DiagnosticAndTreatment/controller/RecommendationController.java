package com.sequitur.api.DiagnosticAndTreatment.controller;

import com.sequitur.api.DiagnosticAndTreatment.domain.model.Recommendation;
import com.sequitur.api.DiagnosticAndTreatment.domain.service.RecommendationService;
import com.sequitur.api.DiagnosticAndTreatment.resource.RecommendationResource;
import com.sequitur.api.DiagnosticAndTreatment.resource.SaveRecommendationResource;
import com.sequitur.api.IdentityAccessManagement.domain.model.Student;
import com.sequitur.api.IdentityAccessManagement.domain.service.StudentService;
import com.sequitur.api.IdentityAccessManagement.resource.SaveStudentResource;
import com.sequitur.api.IdentityAccessManagement.resource.StudentResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "recommendations", description = "Recommendations API")
@RestController
@RequestMapping("/api")
public class RecommendationController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RecommendationService recommendationService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Recommendation returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/students/{studentId}/recommendations")
    public Page<RecommendationResource> getAllRecommendationsByStudentId(
            @PathVariable(name = "studentId") Long studentId,
            Pageable pageable) {
        Page<Recommendation> recommendationPage = recommendationService.getAllRecommendationsByStudentId(studentId, pageable);
        List<RecommendationResource> resources = recommendationPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());
        return new PageImpl<RecommendationResource>(resources, pageable, resources.size());
    }

    @GetMapping("/students/{studentId}/recommendations/{recommendationId}")
    public RecommendationResource getRecommendationByIdAndStudentId(@PathVariable(name = "recommendationId") Long recommendationId,
                                                         @PathVariable(name = "studentId") Long studentId) {
        return convertToResource(recommendationService.getRecommendationByIdAndStudentId(recommendationId, studentId));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Recommendation returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/psychologists/{psychologistId}/recommendations")
    public Page<RecommendationResource> getAllRecommendationsByPsychologistId(
            @PathVariable(name = "psychologistId") Long psychologistId,
            Pageable pageable) {
        Page<Recommendation> recommendationPage = recommendationService.getAllRecommendationsByPsychologistId(psychologistId, pageable);
        List<RecommendationResource> resources = recommendationPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());
        return new PageImpl<RecommendationResource>(resources, pageable, resources.size());
    }

    @GetMapping("/psychologists/{psychologistId}/recommendations/{recommendationId}")
    public RecommendationResource getRecommendationByIdAndPsychologistId(@PathVariable(name = "recommendationId") Long recommendationId,
                                                                    @PathVariable(name = "psychologistId") Long psychologistId) {
        return convertToResource(recommendationService.getRecommendationByIdAndPsychologistId(recommendationId, psychologistId));
    }

    @PostMapping("/psychologists/{psychologistId}/students/{studentId}/recommendations")
    public RecommendationResource createRecommendation(@PathVariable(name = "psychologistId") Long psychologistId,
                                         @PathVariable(name = "studentId") Long studentId,
                                         @Valid @RequestBody SaveRecommendationResource resource) {
        return convertToResource(recommendationService.createRecommendation(psychologistId,studentId, convertToEntity(resource)));

    }

    @PutMapping("/psychologists/{psychologistId}/students/{studentId}/recommendations/{recommendationId}")
    public RecommendationResource updateRecommendation(@PathVariable(name = "psychologistId") Long psychologistId,
                                         @PathVariable(name = "studentId") Long studentId,
                                         @PathVariable(name = "recommendationId") Long recommendationId,
                                         @Valid @RequestBody SaveRecommendationResource resource) {
        return convertToResource(recommendationService.updateRecommendation(recommendationId,psychologistId, studentId, convertToEntity(resource)));
    }

    @DeleteMapping("/psychologists/{psychologistId}/students/{studentId}/recommendations/{recommendationId}")
    public ResponseEntity<?> deleteRecommendation(@PathVariable(name = "psychologistId") Long psychologistId,
                                           @PathVariable(name = "studentId") Long studentId,
                                                  @PathVariable(name = "recommendationId") Long recommendationId)  {
        return recommendationService.deleteRecommendation(recommendationId,psychologistId, studentId);
    }

    @Operation(summary = "Get Recommendations", description = "Get All Recommendations by Pages", tags = { "recommendations" })
    @GetMapping("/recommendations")
    public Page<RecommendationResource> getAllRecommendations(
            @Parameter(description="Pageable Parameter")
            Pageable pageable) {
        Page<Recommendation> recommendationPage = recommendationService.getAllRecommendations(pageable);
        List<RecommendationResource> resources = recommendationPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());

        return new PageImpl<RecommendationResource>(resources,pageable , resources.size());
    }

    private Recommendation convertToEntity(SaveRecommendationResource resource) {
        return mapper.map(resource, Recommendation.class);
    }

    private RecommendationResource convertToResource(Recommendation entity) {
        return mapper.map(entity, RecommendationResource.class);
    }
}
