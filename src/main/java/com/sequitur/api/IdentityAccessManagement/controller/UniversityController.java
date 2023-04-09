package com.sequitur.api.IdentityAccessManagement.controller;


import com.sequitur.api.IdentityAccessManagement.domain.model.University;
import com.sequitur.api.IdentityAccessManagement.domain.service.UniversityService;
import com.sequitur.api.IdentityAccessManagement.resource.SaveUniversityResource;
import com.sequitur.api.IdentityAccessManagement.resource.UniversityResource;
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

@Tag(name = "universities", description = "Universities API")
@RestController
@RequestMapping("/api")
public class UniversityController {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UniversityService universityService;

    @Operation(summary = "Get Universities", description = "Get All Universities by Pages", tags = { "universities" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Universities returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/universities")
    public Page<UniversityResource> getAllUniversities(Pageable pageable) {
        Page<University> universitiesPage = universityService.getAllUniversities(pageable);
        List<UniversityResource> resources = universitiesPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());

        return new PageImpl<>(resources, pageable, resources.size());
    }

    @Operation(summary = "Get University by Id", description = "Get a University by specifying Id", tags = { "universities" })
    @GetMapping("/universities/{id}")
    public UniversityResource getUniversityById(
            @Parameter(description="University Id")
            @PathVariable(name = "id") Long universityId) {
        return convertToResource(universityService.getUniversityById(universityId));
    }

    @PostMapping("/universities")
    public UniversityResource createUniversity(@Valid @RequestBody SaveUniversityResource resource)  {
        University university = convertToEntity(resource);
        return convertToResource(universityService.createUniversity(university));
    }

    @PutMapping("/universities/{id}")
    public UniversityResource updateUniversity(@PathVariable(name = "id") Long universityId, @Valid @RequestBody SaveUniversityResource resource) {
        University university = convertToEntity(resource);
        return convertToResource(universityService.updateUniversity(universityId, university));
    }

    @DeleteMapping("/universities/{id}")
    public ResponseEntity<?> deleteUniversity(@PathVariable(name = "id") Long universityId) {
        return universityService.deleteUniversity(universityId);
    }

    @GetMapping("/universities/ruc/{ruc}")
    public UniversityResource getUniversityByRuc(@PathVariable(name = "ruc") String ruc) {
        return convertToResource( universityService.getUniversityByRuc(ruc));
    }
    // Auto Mapper
    private University convertToEntity(SaveUniversityResource resource) {
        return mapper.map(resource, University.class);
    }

    private UniversityResource convertToResource(University entity) {
        return mapper.map(entity, UniversityResource.class);
    }
}
