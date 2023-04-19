package com.sequitur.api.ProactiveCommunication.domain.service;

import com.sequitur.api.DataCollection.domain.model.Response;
import com.sequitur.api.ProactiveCommunication.domain.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface NotificationService {

    ResponseEntity<?> deleteNotification(Long studentId, Long notificationId);

    Notification updateNotification(Long studentId, Long notificationId, Notification notificationRequest);

    Notification createNotification(Long studentId, Notification notification);

    Notification getNotificationById(Long notificationId);

    Page<Notification> getAllNotifications(Pageable pageable);

    Page<Notification> getAllNotificationsByStudentId(Long studentId, Pageable pageable);

    Notification getNotificationByIdAndStudentId(Long studentId, Long notificationId);
}
