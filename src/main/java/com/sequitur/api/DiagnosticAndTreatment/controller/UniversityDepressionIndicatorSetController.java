package com.sequitur.api.DiagnosticAndTreatment.controller;

import com.sequitur.api.DiagnosticAndTreatment.domain.model.UniversityDepressionIndicatorSet;
import com.sequitur.api.DiagnosticAndTreatment.domain.service.UniversityDepressionIndicatorSetService;
import com.sequitur.api.DiagnosticAndTreatment.resource.SaveUniversityDepressionIndicatorSetResource;
import com.sequitur.api.DiagnosticAndTreatment.resource.UniversityDepressionIndicatorSetResource;
import com.sequitur.api.IdentityAccessManagement.domain.model.Student;
import com.sequitur.api.IdentityAccessManagement.resource.StudentResource;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "depressionSets", description = "Depression Sets API")
@RestController
@RequestMapping("/api")
public class UniversityDepressionIndicatorSetController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UniversityDepressionIndicatorSetService universityDepressionIndicatorSetService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Depression Sets returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/university/{universityId}/depressionIndicators")
    public UniversityDepressionIndicatorSetResource getAllUniversityDepressionIndicatorSetsByUniversityId(
            @PathVariable(name = "universityId") Long universityId) {
        return convertToResource(universityDepressionIndicatorSetService.getUniversityDepressionIndicatorSetsByUniversity(universityId));
    }

    @PutMapping("/universities/{universityId}/depressionIndicators/{depressionIndicatorId}")
    public UniversityDepressionIndicatorSetResource updateUniversityDepressionIndicatorSet(@PathVariable(name = "universityId") Long universityId,
                                                       @PathVariable(name = "depressionSetId") Long depressionSetId,
                                                       @Valid @RequestBody SaveUniversityDepressionIndicatorSetResource resource) {
        return convertToResource(universityDepressionIndicatorSetService.updateDepressionSet(universityId,depressionSetId,convertToEntity(resource)));
    }


    private UniversityDepressionIndicatorSet convertToEntity(SaveUniversityDepressionIndicatorSetResource resource) {
        return mapper.map(resource, UniversityDepressionIndicatorSet.class);
    }

    private UniversityDepressionIndicatorSetResource convertToResource(UniversityDepressionIndicatorSet entity) {
        return mapper.map(entity, UniversityDepressionIndicatorSetResource.class);
    }
}
