package com.sequitur.api.IdentityAccessManagement.controller;

import com.sequitur.api.IdentityAccessManagement.domain.model.Psychologist;
import com.sequitur.api.IdentityAccessManagement.domain.model.Student;
import com.sequitur.api.IdentityAccessManagement.domain.service.PsychologistService;
import com.sequitur.api.IdentityAccessManagement.domain.service.StudentService;
import com.sequitur.api.IdentityAccessManagement.resource.PsychologistResource;
import com.sequitur.api.IdentityAccessManagement.resource.SavePsychologistResource;
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

@Tag(name = "psychologists", description = "Psychologists API")
@RestController
@RequestMapping("/api")
public class PsychologistController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PsychologistService psychologistService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Psychologists returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/universities/{universityId}/psychologists")
    public Page<PsychologistResource> getAllPsychologistsByUniversityId(
            @PathVariable(name = "universityId") Long universityId,
            Pageable pageable) {
        Page<Psychologist> psychologistPage = psychologistService.getAllPsychologistsByUniversityId(universityId, pageable);
        List<PsychologistResource> resources = psychologistPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());
        return new PageImpl<PsychologistResource>(resources, pageable, resources.size());
    }

    @GetMapping("/universities/{universityId}/psychologists/{psychologistId}")
    public PsychologistResource getSPsychologistByIdAndUniversityId(@PathVariable(name = "universityId") Long universityId,
                                                         @PathVariable(name = "psychologistId") Long psychologistId) {
        return convertToResource(psychologistService.getPsychologistByIdAndUniversityId(universityId, psychologistId));
    }

    @PostMapping("/universities/{universityId}/psychologists")
    public PsychologistResource createPsychologist(@PathVariable(name = "universityId") Long universityId,
                                         @Valid @RequestBody SavePsychologistResource resource) {
        return convertToResource(psychologistService.createPsychologist(universityId, convertToEntity(resource)));

    }

    @PutMapping("/universities/{universityId}/psychologists/{psychologistId}")
    public PsychologistResource updatePsychologist(@PathVariable(name = "universityId") Long universityId,
                                         @PathVariable(name = "psychologistId") Long psychologistId,
                                         @Valid @RequestBody SavePsychologistResource resource) {
        return convertToResource(psychologistService.updatePsychologist(universityId, psychologistId, convertToEntity(resource)));
    }

    @DeleteMapping("/universities/{universityId}/psychologists/{psychologistId}")
    public ResponseEntity<?> deletePsychologist(@PathVariable(name = "universityId") Long universityId,
                                           @PathVariable(name = "psychologistId") Long psychologistId) {
        return psychologistService.deletePsychologist(universityId, psychologistId);
    }

    @Operation(summary = "Get Psychologists", description = "Get All Psychologists by Pages", tags = { "psychologists" })
    @GetMapping("/psychologists")
    public Page<PsychologistResource> getAllPsychologists(
            @Parameter(description="Pageable Parameter")
            Pageable pageable) {
        Page<Psychologist> psychologistPage = psychologistService.getAllPsychologists(pageable);
        List<PsychologistResource> resources = psychologistPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());

        return new PageImpl<PsychologistResource>(resources,pageable , resources.size());
    }

    private Psychologist convertToEntity(SavePsychologistResource resource) {
        return mapper.map(resource, Psychologist.class);
    }

    private PsychologistResource convertToResource(Psychologist entity) {
        return mapper.map(entity, PsychologistResource.class);
    }
}
