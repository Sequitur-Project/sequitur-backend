package com.sequitur.api.ProactiveCommunication.service;

import com.sequitur.api.ProactiveCommunication.domain.model.Support;
import com.sequitur.api.ProactiveCommunication.domain.repository.SupportRepository;
import com.sequitur.api.ProactiveCommunication.domain.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupportServiceImpl implements SupportService {

    @Autowired
    private SupportRepository supportRepository;

    @Override
    public List<Support> getAllSupport() {
       return supportRepository.findAll();
    }

    @Override
    public Support createSupport(Support support) {
        return supportRepository.save(support);
    }
}
