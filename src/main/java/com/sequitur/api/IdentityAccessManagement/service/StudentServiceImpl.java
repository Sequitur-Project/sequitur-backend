package com.sequitur.api.IdentityAccessManagement.service;

import com.sequitur.api.IdentityAccessManagement.domain.model.Student;
import com.sequitur.api.IdentityAccessManagement.domain.model.University;
import com.sequitur.api.IdentityAccessManagement.domain.repository.StudentRepository;
import com.sequitur.api.IdentityAccessManagement.domain.repository.UniversityRepository;
import com.sequitur.api.IdentityAccessManagement.domain.service.StudentService;
import com.sequitur.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Override
    public Page<Student> getAllStudentsByUniversityId(Long universityId, Pageable pageable) {
        return studentRepository.findByUniversityId(universityId, pageable);
    }

    @Override
    public Student getStudentByIdAndUniversityId(Long universityId, Long studentId) {
        return studentRepository.findByIdAndUniversityId(studentId, universityId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found with Id " + studentId +
                                " and UniversityId " + universityId));
    }

    @Override
    public ResponseEntity<?> deleteStudent(Long universityId, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "Id", studentId));
        studentRepository.delete(student);
        return ResponseEntity.ok().build();
    }

    @Override
    public Student updateStudent(Long universityId, Long studentId, Student studentRequest) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "Id", studentId));
       student.setFirstName(studentRequest.getFirstName());
       student.setLastName(studentRequest.getLastName());
       student.setEmail(studentRequest.getEmail());
       student.setPassword(studentRequest.getPassword());
       student.setTelephone(studentRequest.getTelephone());
       student.setBirthDate(studentRequest.getBirthDate());

        return studentRepository.save(student);
    }

    @Override
    public Student createStudent(Long universityId, Student student) {
        University university = universityRepository.findById(universityId).orElseThrow(() -> new ResourceNotFoundException("University", "Id", universityId));
        student.setUniversity(university);
        return studentRepository.save(student);
    }

    @Override
    public Student getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "Id", studentId));
    }

    @Override
    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }
}
