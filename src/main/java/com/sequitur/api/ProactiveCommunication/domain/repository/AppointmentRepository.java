package com.sequitur.api.ProactiveCommunication.domain.repository;

import com.sequitur.api.ProactiveCommunication.domain.model.Appointment;
import com.sequitur.api.ProactiveCommunication.domain.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> findByPsychologistId(Long psychologistId);

    Optional<Appointment> findByIdAndPsychologistId(Long psychologistId, Long appointmentId);

    Page<Appointment> findByStudentId(Long studentId, Pageable pageable);

    Optional<Appointment> findByIdAndStudentId(Long studentId, Long appointmentId);

}
