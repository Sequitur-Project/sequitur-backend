package com.sequitur.api.ProactiveCommunication.service;

import com.sequitur.api.IdentityAccessManagement.domain.model.Student;
import com.sequitur.api.IdentityAccessManagement.domain.repository.StudentRepository;
import com.sequitur.api.ProactiveCommunication.domain.model.Notification;
import com.sequitur.api.ProactiveCommunication.domain.repository.NotificationRepository;
import com.sequitur.api.ProactiveCommunication.domain.service.NotificationService;
import com.sequitur.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public ResponseEntity<?> deleteNotification(Long studentId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "Id", notificationId));
        notificationRepository.delete(notification);
        return ResponseEntity.ok().build();
    }

    @Override
    public Notification updateNotification(Long studentId, Long notificationId, Notification notificationRequest) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "Id", notificationId));
        notification.setTitle(notificationRequest.getTitle());
        notification.setMessage(notificationRequest.getMessage());
        return notificationRepository.save(notification);
    }

    @Override
    public Notification createNotification(Long studentId, Notification notification) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student", "Id", studentId));
        notification.setStudent(student);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification getNotificationById(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "Id", notificationId));
    }

    @Override
    public Page<Notification> getAllNotifications(Pageable pageable) {
        return notificationRepository.findAll(pageable);
    }

    @Override
    public Page<Notification> getAllNotificationsByStudentId(Long studentId, Pageable pageable) {
        return notificationRepository.findByStudentId(studentId, pageable);
    }

    @Override
    public Notification getNotificationByIdAndStudentId(Long studentId, Long notificationId) {
        return notificationRepository.findByIdAndStudentId(studentId, notificationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found with Id " + notificationId +
                                " and StudentId " + studentId));
    }
}
