package com.sequitur.api.DiagnosticAndTreatment.service;

import com.sequitur.api.DiagnosticAndTreatment.domain.model.Result;
import com.sequitur.api.DiagnosticAndTreatment.domain.model.UniversityDepressionIndicatorSet;
import com.sequitur.api.DiagnosticAndTreatment.domain.repository.ResultRepository;
import com.sequitur.api.DiagnosticAndTreatment.domain.repository.UniversityDepressionIndicatorSetRepository;
import com.sequitur.api.DiagnosticAndTreatment.domain.service.UniversityDepressionIndicatorSetService;
import com.sequitur.api.IdentityAccessManagement.domain.model.Student;
import com.sequitur.api.IdentityAccessManagement.domain.model.University;
import com.sequitur.api.IdentityAccessManagement.domain.repository.StudentRepository;
import com.sequitur.api.IdentityAccessManagement.domain.repository.UniversityRepository;
import com.sequitur.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniversityDepressionIndicatorSetServiceImpl implements UniversityDepressionIndicatorSetService {

    @Autowired
    private UniversityRepository universityRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private UniversityDepressionIndicatorSetRepository universityDepressionIndicatorSetRepository;


    @Override
    public UniversityDepressionIndicatorSet updateDepressionSet(Long statisticId, Long depressionSetId, UniversityDepressionIndicatorSet universityDepressionIndicatorSetRequest) {
        UniversityDepressionIndicatorSet universityDepressionIndicatorSet = universityDepressionIndicatorSetRepository.findById(depressionSetId)
                .orElseThrow(() -> new ResourceNotFoundException("DepressionSet", "id", depressionSetId));

        universityDepressionIndicatorSet.setDepressionPercentage(universityDepressionIndicatorSetRequest.getDepressionPercentage());
        universityDepressionIndicatorSet.setNoDepressionPercentage(universityDepressionIndicatorSetRequest.getNoDepressionPercentage());
        universityDepressionIndicatorSet.setStudentsQuantity(universityDepressionIndicatorSetRequest.getStudentsQuantity());

        return universityDepressionIndicatorSetRepository.save(universityDepressionIndicatorSet);
    }

    @Override
    public UniversityDepressionIndicatorSet getUniversityDepressionIndicatorSetsByUniversity(Long universityId) {
        return universityDepressionIndicatorSetRepository.findAllByUniversityId(universityId);
    }
}
