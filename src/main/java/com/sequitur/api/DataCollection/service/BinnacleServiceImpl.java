package com.sequitur.api.DataCollection.service;

import com.sequitur.api.DataCollection.domain.model.Binnacle;
import com.sequitur.api.DataCollection.domain.model.Response;
import com.sequitur.api.DataCollection.domain.repository.BinnacleRepository;
import com.sequitur.api.DataCollection.domain.service.BinnacleService;
import com.sequitur.api.IdentityAccessManagement.domain.model.Psychologist;
import com.sequitur.api.IdentityAccessManagement.domain.model.Student;
import com.sequitur.api.IdentityAccessManagement.domain.model.University;
import com.sequitur.api.IdentityAccessManagement.domain.repository.StudentRepository;
import com.sequitur.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BinnacleServiceImpl implements BinnacleService {

    @Autowired
    private BinnacleRepository binnacleRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public ResponseEntity<?> deleteBinnacle(Long binnacleId, Long studentId) {
        Binnacle binnacle = binnacleRepository.findById(binnacleId)
                .orElseThrow(() -> new ResourceNotFoundException("Binnacle", "Id", binnacleId));
        binnacleRepository.delete(binnacle);
        return ResponseEntity.ok().build();
    }


    @Override
    public Binnacle createBinnacle(Long studentId, Binnacle binnacle) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student", "Id", studentId));
        binnacle.setStudent(student);
        return binnacleRepository.save(binnacle);
    }

    @Override
    public Page<Binnacle> getAllBinnacles(Pageable pageable) {
        return binnacleRepository.findAll(pageable);
    }

    @Override
    public Binnacle getBinnacleByIdAndStudentId(Long binnacleId, Long studentId) {
        return binnacleRepository.findByIdAndStudentId(binnacleId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Binnacle not found with Id " + binnacleId +
                                " and StudentId " + studentId));
    }

    @Override
    public Binnacle getByStudentId(Long studentId) {
        return binnacleRepository.findByStudentId(studentId);
    }
}
