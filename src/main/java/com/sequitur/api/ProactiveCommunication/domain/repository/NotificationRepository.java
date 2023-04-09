package com.sequitur.api.ProactiveCommunication.domain.repository;

import com.sequitur.api.DataCollection.domain.model.Response;
import com.sequitur.api.ProactiveCommunication.domain.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByStudentId(Long studentId, Pageable pageable);

    Optional<Notification> findByIdAndStudentId(Long studentId, Long notificationId);

}
