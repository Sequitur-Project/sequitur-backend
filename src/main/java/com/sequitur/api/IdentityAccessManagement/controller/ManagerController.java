package com.sequitur.api.IdentityAccessManagement.controller;

import com.sequitur.api.IdentityAccessManagement.domain.model.Manager;
import com.sequitur.api.IdentityAccessManagement.domain.model.Psychologist;
import com.sequitur.api.IdentityAccessManagement.domain.model.University;
import com.sequitur.api.IdentityAccessManagement.domain.service.ManagerService;
import com.sequitur.api.IdentityAccessManagement.domain.service.UniversityService;
import com.sequitur.api.IdentityAccessManagement.resource.ManagerResource;
import com.sequitur.api.IdentityAccessManagement.resource.SaveManagerResource;
import com.sequitur.api.IdentityAccessManagement.resource.SavePsychologistResource;
import com.sequitur.api.IdentityAccessManagement.resource.UpdateManagerResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "managers", description = "Managers API")
@RestController
@RequestMapping("/api")
public class ManagerController {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ManagerService managerService;
    @Autowired
    private UniversityService universityService;

    @GetMapping("/managers/{managerId}/status")
    public boolean isSubscribed(@PathVariable Long managerId) {
        return managerService.isSubscribed(managerId);
    }

    @Operation(summary = "Get Managers", description = "Get All Managers by Pages", tags = { "managers" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Managers returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/managers")
    public Page<ManagerResource> getAllManagers(Pageable pageable) {
        Page<Manager> managersPage = managerService.getAllManagers(pageable);
        List<ManagerResource> resources = managersPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());

        return new PageImpl<>(resources, pageable, resources.size());
    }

    @Operation(summary = "Get Manager by Id", description = "Get a Manager by specifying Id", tags = { "managers" })
    @GetMapping("/managers/{id}")
    public ManagerResource getManagerById(
            @Parameter(description="Manager Id")
            @PathVariable(name = "id") Long managerId) {
        return convertToResource(managerService.getManagerById(managerId));
    }

    @Operation(summary = "Get Manager by Email And Password And Role", description = "Get a Manager by specifying email and password and role", tags = { "managers" })
    @GetMapping("/managers/login")
    public ManagerResource getManagerByEmailAndPasswordAndRole(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "role") String role) {
        return convertToResource(managerService.findByEmailAndPasswordAndRole(email, password,role));
    }

    @PostMapping("/managers")
    @Transactional
    public ManagerResource createManager(@Valid @RequestBody SaveManagerResource resource)  {
        Manager manager = convertToEntity(resource);
        manager.setRole("Manager");
        return convertToResource(managerService.createManager(manager));
    }

    @PostMapping("/managers/{managerId}/subscriptions/{subscriptionId}")
    public ManagerResource setManagerSubscription(@PathVariable Long managerId, @PathVariable Long subscriptionId) {
       return convertToResource(managerService.setManagerSubscription(managerId, subscriptionId));
    }

    @PutMapping("/managers/{id}")
    public ManagerResource updateManager(@PathVariable(name = "id") Long managerId, @Valid @RequestBody UpdateManagerResource resource) {
        Manager manager = convertToEntityUpdated(resource);
        return convertToResource(managerService.updateManager(managerId, manager));
    }

    @DeleteMapping("/managers/{id}")
    public ResponseEntity<?> deleteManager(@PathVariable(name = "id") Long managerId) {
        return managerService.deleteManager(managerId);
    }
    // Auto Mapper
    private Manager convertToEntity(SaveManagerResource resource) {
        Manager manager = mapper.map(resource, Manager.class);
        if (resource.getUniversity() != null) {
            University university = mapper.map(resource.getUniversity(), University.class);
            manager.setUniversity(university);
        }
        return manager;
    }

    private Manager convertToEntityUpdated(UpdateManagerResource resource) {
        return mapper.map(resource, Manager.class);
    }
    private ManagerResource convertToResource(Manager entity) {
        return mapper.map(entity, ManagerResource.class);
    }
}
