package com.sequitur.api.ProactiveCommunication.service;

import com.sequitur.api.IdentityAccessManagement.domain.model.Psychologist;
import com.sequitur.api.IdentityAccessManagement.domain.model.Student;
import com.sequitur.api.IdentityAccessManagement.domain.repository.PsychologistRepository;
import com.sequitur.api.IdentityAccessManagement.domain.repository.StudentRepository;
import com.sequitur.api.ProactiveCommunication.domain.model.Appointment;
import com.sequitur.api.ProactiveCommunication.domain.model.Notification;
import com.sequitur.api.ProactiveCommunication.domain.repository.AppointmentRepository;
import com.sequitur.api.ProactiveCommunication.domain.repository.NotificationRepository;
import com.sequitur.api.ProactiveCommunication.domain.service.AppointmentService;
import com.sequitur.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PsychologistRepository psychologistRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public ResponseEntity<?> deleteAppointment(Long psychologistId, Long studentId, Long appointmentId) {
        Appointment appointment =appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "Id",appointmentId));
        appointmentRepository.delete(appointment);
        return ResponseEntity.ok().build();
    }

    @Override
    public Appointment updateAppointment(Long studentId, Long psychologistId, Long appointmentId, Appointment appointmentRequest) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "Id", appointmentId));
        appointment.setAppointmentDate(appointmentRequest.getAppointmentDate());
        appointment.setAppointmentLocation(appointmentRequest.getAppointmentLocation());
        appointment.setAppointmentTime(appointmentRequest.getAppointmentTime());
        appointment.setReason(appointment.getReason());

        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment patchAppointment(Long studentId, Long psychologistId, Long appointmentId, Appointment appointmentRequest) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "Id", appointmentId));
        appointment.setAccepted(appointmentRequest.isAccepted());

        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment createAppointment(Long psychologistId, Long studentId, Appointment appointment) {
        // fetch the psychologist and student from the repository
        Psychologist psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist", "id", psychologistId));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        // associate the appointment with the psychologist and student
        appointment.setPsychologist(psychologist);
        appointment.setStudent(student);

        // save the appointment to the repository
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // send a notification to the student
        Notification notification = new Notification();
        notification.setTitle("Aviso de Cita");
        notification.setMessage("El psicologo"+ psychologist.getFirstName() + psychologist.getLastName() + "te ha programado una cita");
        notification.setStudent(student);
        notificationRepository.save(notification);
        return savedAppointment;
    }

    @Override
    public Page<Appointment> getAllAppointments(Pageable pageable) {
        return appointmentRepository.findAll(pageable);
    }

    @Override
    public List<Appointment> getAllAppointmentsByPsychologistId(Long psychologistId) {
        return appointmentRepository.findByPsychologistId(psychologistId);
    }

    @Override
    public Appointment getAppointmentByIdAndPsychologistId(Long psychologistId, Long appointmentId) {
        return appointmentRepository.findByIdAndPsychologistId(psychologistId, appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with Id " + appointmentId +
                                " and PsychologistId " + psychologistId));
    }

    @Override
    public Page<Appointment> getAllAppointmentsByStudentId(Long studentId, Pageable pageable) {
        return appointmentRepository.findByStudentId(studentId, pageable);
    }

    @Override
    public Appointment getAppointmentByIdAndStudentId(Long studentId, Long appointmentId) {
        return appointmentRepository.findByIdAndStudentId(studentId, appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with Id " + appointmentId +
                                " and StudentId " + studentId));
    }
}
