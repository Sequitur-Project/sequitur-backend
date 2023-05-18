package com.sequitur.api.ProactiveCommunication.controller;

import com.sequitur.api.IdentityAccessManagement.domain.model.University;
import com.sequitur.api.ProactiveCommunication.domain.model.Appointment;
import com.sequitur.api.ProactiveCommunication.domain.model.Notification;
import com.sequitur.api.ProactiveCommunication.domain.model.Support;
import com.sequitur.api.ProactiveCommunication.domain.service.SupportService;
import com.sequitur.api.ProactiveCommunication.resource.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Tag(name = "supports", description = "Supports API")
@RestController
@RequestMapping("/api")
public class SupportController {
    @Autowired
    private SupportService supportService;
    @Autowired
    private ModelMapper mapper;

    @PostMapping("/supports")
    public SupportResource createSupportTicket(@RequestBody SaveSupportResource resource) {
        return convertToResource(supportService.createSupport(convertToEntity(resource)));

    }
    @Operation(summary = "Get Supports", description = "Get All Supports", tags = { "supports" })
    @GetMapping("/supports")
    public List<SupportResource> getAllSupportTickets(){
        List<Support> supports = supportService.getAllSupport();
        return supports.stream().map(this::convertToResource).collect(Collectors.toList());
    }

    private Support convertToEntity(SaveSupportResource resource) {
        return mapper.map(resource, Support.class);
    }

    private SupportResource convertToResource(Support entity) {
        return mapper.map(entity, SupportResource.class);
    }
}
