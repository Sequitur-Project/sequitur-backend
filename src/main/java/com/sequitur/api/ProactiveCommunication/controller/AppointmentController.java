package com.sequitur.api.ProactiveCommunication.controller;

import com.sequitur.api.ProactiveCommunication.domain.model.Appointment;
import com.sequitur.api.ProactiveCommunication.domain.model.Notification;
import com.sequitur.api.ProactiveCommunication.domain.service.AppointmentService;
import com.sequitur.api.ProactiveCommunication.domain.service.NotificationService;
import com.sequitur.api.ProactiveCommunication.resource.*;
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

@Tag(name = "appointments", description = "Appointments API")
@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AppointmentService appointmentService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Appointments returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/students/{studentId}/appointments")
    public Page<AppointmentResource> getAllAppointmentsByStudentId(
            @PathVariable(name = "studentId") Long studentId,
            Pageable pageable) {
        Page<Appointment> appointmentPage = appointmentService.getAllAppointmentsByStudentId(studentId, pageable);
        List<AppointmentResource> resources = appointmentPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());
        return new PageImpl<AppointmentResource>(resources, pageable, resources.size());
    }

    @GetMapping("/students/{studentId}/appointments/{appointmentId}")
    public AppointmentResource getAppointmentByIdAndStudentId(@PathVariable(name = "studentId") Long studentId,
                                                                @PathVariable(name = "appointmentId") Long appointmentId) {
        return convertToResource(appointmentService.getAppointmentByIdAndStudentId(studentId, appointmentId));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Appointments returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/psychologists/{psychologistId}/appointments")
    public Page<AppointmentResource> getAllAppointmentsByPsychologistId(
            @PathVariable(name = "psychologistId") Long psychologistId,
            Pageable pageable) {
        Page<Appointment> appointmentPage = appointmentService.getAllAppointmentsByPsychologistId(psychologistId, pageable);
        List<AppointmentResource> resources = appointmentPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());
        return new PageImpl<AppointmentResource>(resources, pageable, resources.size());
    }

    @GetMapping("/psychologists/{psychologistId}/appointments/{appointmentId}")
    public AppointmentResource getAppointmentByIdAndPsychologistId(@PathVariable(name = "psychologistId") Long psychologistId,
                                                              @PathVariable(name = "appointmentId") Long appointmentId) {
        return convertToResource(appointmentService.getAppointmentByIdAndPsychologistId(psychologistId, appointmentId));
    }

    @PostMapping("/psychologists/{psychologistId}/students/{studentId}/appointments")
    public AppointmentResource createAppointment(@PathVariable(name = "studentId") Long studentId,
                                                 @PathVariable(name = "psychologistId") Long psychologistId,
                                                   @Valid @RequestBody SaveAppointmentResource resource) {
        return convertToResource(appointmentService.createAppointment(psychologistId,studentId, convertToEntity(resource)));

    }

    @PatchMapping("/students/{studentId}/psychologists/{psychologistId}/appointments/{appointmentId}")
    public AppointmentResource patchAppointment(@PathVariable(name = "studentId") Long studentId,
                                                 @PathVariable(name = "psychologistId") Long psychologistId,
                                                   @PathVariable(name = "appointmentId") Long appointmentId,
                                                   @Valid @RequestBody UpdateAppointmentResource resource) {
        return convertToResource(appointmentService.patchAppointment(studentId,psychologistId, appointmentId, convertToEntity(resource)));
    }

    @PutMapping("/psychologists/{psychologistId}/students/{studentId}/appointments/{appointmentId}")
    public AppointmentResource updateAppointment(@PathVariable(name = "studentId") Long studentId,
                                                @PathVariable(name = "psychologistId") Long psychologistId,
                                                @PathVariable(name = "appointmentId") Long appointmentId,
                                                @Valid @RequestBody SaveAppointmentResource resource) {
        return convertToResource(appointmentService.updateAppointment(studentId,psychologistId, appointmentId, convertToEntity(resource)));
    }

    @DeleteMapping("/psychologists/{psychologistId}/students/{studentId}/appointments/{appointmentId}")
    public ResponseEntity<?> deleteAppointment(@PathVariable(name = "studentId") Long studentId,
                                               @PathVariable(name = "psychologistId") Long psychologistId,
                                                @PathVariable(name = "appointmentId") Long appointmentId) {
        return appointmentService.deleteAppointment(psychologistId,studentId, appointmentId);
    }

    @Operation(summary = "Get Appointments", description = "Get All Appointments by Pages", tags = { "appointments" })
    @GetMapping("/appointments")
    public Page<AppointmentResource> getAllAppointments(
            @Parameter(description="Pageable Parameter")
            Pageable pageable) {
        Page<Appointment> appointmentPage = appointmentService.getAllAppointments(pageable);
        List<AppointmentResource> resources = appointmentPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());

        return new PageImpl<AppointmentResource>(resources,pageable , resources.size());
    }

    private Appointment convertToEntity(SaveAppointmentResource resource) {
        return mapper.map(resource, Appointment.class);
    }

    private Appointment convertToEntity(UpdateAppointmentResource resource) {
        return mapper.map(resource, Appointment.class);
    }

    private AppointmentResource convertToResource(Appointment entity) {
        return mapper.map(entity, AppointmentResource.class);
    }
}
