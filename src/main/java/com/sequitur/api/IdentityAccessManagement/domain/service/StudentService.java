package com.sequitur.api.IdentityAccessManagement.domain.service;

import com.sequitur.api.IdentityAccessManagement.domain.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface StudentService {

    Page<Student> getAllStudentsByUniversityId(Long universityId, Pageable pageable);

    Student getStudentByIdAndUniversityId(Long universityId, Long studentId);

    ResponseEntity<?> deleteStudent(Long universityId, Long studentId);

    Student updateStudent(Long universityId, Long studentId, Student studentRequest);

    Student createStudent(Long universityId, Student student);

    Student getStudentById(Long studentId);

    Page<Student> getAllStudents(Pageable pageable);
}
