package com.sequitur.api.IdentityAccessManagement.service;

import com.sequitur.api.IdentityAccessManagement.domain.model.Manager;
import com.sequitur.api.IdentityAccessManagement.domain.model.University;
import com.sequitur.api.IdentityAccessManagement.domain.repository.ManagerRepository;
import com.sequitur.api.IdentityAccessManagement.domain.repository.UniversityRepository;
import com.sequitur.api.IdentityAccessManagement.domain.service.ManagerService;
import com.sequitur.api.Subscriptions.domain.model.Subscription;
import com.sequitur.api.Subscriptions.domain.repository.SubscriptionRepository;
import com.sequitur.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public Manager setManagerSubscription(Long managerId, Long subscriptionId) {
        Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new ResourceNotFoundException("Manager", "Id", managerId));

        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(() -> new ResourceNotFoundException("Subscription", "Id", subscriptionId));

        manager.setSubscription(subscription);
       return managerRepository.save(manager);
    }

    @Override
    public ResponseEntity<?> deleteManager(Long managerId) {
        Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new ResourceNotFoundException("Manager", "Id", managerId));
        managerRepository.delete(manager);
        return ResponseEntity.ok().build();
    }

    @Override
    public Manager updateManager(Long managerId, Manager managerRequest) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager", "Id", managerId));
        manager.setEmail(managerRequest.getEmail());
        manager.setFirstName(managerRequest.getFirstName());
        manager.setLastName(managerRequest.getLastName());
        manager.setPassword(managerRequest.getPassword());
        manager.setTelephone(managerRequest.getTelephone());
        manager.setUniversity(managerRequest.getUniversity());
        return managerRepository.save(manager);
    }

    @Override
    public Manager createManager(Manager manager) {
        University university = manager.getUniversity();
        universityRepository.save(university); // Save the University instance first
        return managerRepository.save(manager); // Save the Manager instance next
    }

    @Override
    public Manager getManagerById(Long managerId) {
        Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new ResourceNotFoundException("Manager", "Id", managerId));
        return manager;
    }

    @Override
    public Page<Manager> getAllManagers(Pageable pageable) {
        return managerRepository.findAll(pageable);
    }
}
