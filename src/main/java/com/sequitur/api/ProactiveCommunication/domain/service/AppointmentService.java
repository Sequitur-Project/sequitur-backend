package com.sequitur.api.ProactiveCommunication.domain.service;

import com.sequitur.api.ProactiveCommunication.domain.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AppointmentService {
    ResponseEntity<?> deleteAppointment(Long psychologistId, Long studentId, Long appointmentId);

    Appointment updateAppointment(Long studentId, Long psychologistId, Long appointmentId, Appointment appointmentRequest);

    Appointment patchAppointment(Long studentId, Long psychologistId, Long appointmentId, Appointment appointmentRequest);

    Appointment createAppointment(Long psychologistId,Long studentId, Appointment appointment);

    Page<Appointment> getAllAppointments(Pageable pageable);

    List<Appointment> getAllAppointmentsByPsychologistId(Long psychologistId);

    Appointment getAppointmentByIdAndPsychologistId(Long psychologistId, Long appointmentId);

    Page<Appointment> getAllAppointmentsByStudentId(Long studentId, Pageable pageable);

    Appointment getAppointmentByIdAndStudentId(Long studentId, Long appointmentId);
}
