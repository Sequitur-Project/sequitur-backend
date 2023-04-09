package com.sequitur.api.ProactiveCommunication.controller;

import com.sequitur.api.ProactiveCommunication.domain.model.Notification;
import com.sequitur.api.ProactiveCommunication.domain.service.NotificationService;
import com.sequitur.api.ProactiveCommunication.resource.NotificationResource;
import com.sequitur.api.ProactiveCommunication.resource.SaveNotificationResource;
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

@Tag(name = "notifications", description = "Notifications API")
@RestController
@RequestMapping("/api")
public class NotificationController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private NotificationService notificationService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Notifications returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/students/{studentId}/notifications")
    public Page<NotificationResource> getAllNotificationsByUserId(
            @PathVariable(name = "studentId") Long studentId,
            Pageable pageable) {
        Page<Notification> notificationPage = notificationService.getAllNotificationsByStudentId(studentId, pageable);
        List<NotificationResource> resources = notificationPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());
        return new PageImpl<NotificationResource>(resources, pageable, resources.size());
    }

    @GetMapping("/students/{studentId}/notifications/{notificationId}")
    public NotificationResource getNotificationByIdAndStudentId(@PathVariable(name = "studentId") Long studentId,
                                             @PathVariable(name = "notificationId") Long notificationId) {
        return convertToResource(notificationService.getNotificationByIdAndStudentId(studentId, notificationId));
    }

    @PostMapping("/students/{studentId}/notifications")
    public NotificationResource createNotification(@PathVariable(name = "studentId") Long studentId,
                                   @Valid @RequestBody SaveNotificationResource resource) {
        return convertToResource(notificationService.createNotification(studentId, convertToEntity(resource)));

    }

    @PutMapping("/students/{studentId}/notifications/{notificationId}")
    public NotificationResource updateNotification(@PathVariable(name = "studentId") Long studentId,
                                   @PathVariable(name = "notificationId") Long notificationId,
                                   @Valid @RequestBody SaveNotificationResource resource) {
        return convertToResource(notificationService.updateNotification(studentId, notificationId, convertToEntity(resource)));
    }

    @DeleteMapping("/users/{userId}/posts/{postId}")
    public ResponseEntity<?> deleteNotification(@PathVariable(name = "studentId") Long studentId,
                                        @PathVariable(name = "notificationId") Long notificationId) {
        return notificationService.deleteNotification(studentId, notificationId);
    }

    @Operation(summary = "Get Notifications", description = "Get All Notifications by Pages", tags = { "notifications" })
    @GetMapping("/notifications")
    public Page<NotificationResource> getAllNotifications(
            @Parameter(description="Pageable Parameter")
            Pageable pageable) {
        Page<Notification> notificationsPage = notificationService.getAllNotifications(pageable);
        List<NotificationResource> resources = notificationsPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());

        return new PageImpl<NotificationResource>(resources,pageable , resources.size());
    }

    private Notification convertToEntity(SaveNotificationResource resource) {
        return mapper.map(resource, Notification.class);
    }

    private NotificationResource convertToResource(Notification entity) {
        return mapper.map(entity, NotificationResource.class);
    }
}
