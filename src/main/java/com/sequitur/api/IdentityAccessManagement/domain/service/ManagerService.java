package com.sequitur.api.IdentityAccessManagement.domain.service;

import com.sequitur.api.IdentityAccessManagement.domain.model.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ManagerService {
    ResponseEntity<?> deleteManager(Long managerId);

    Manager updateManager(Long managerId, Manager managerRequest);

    Manager createManager(Manager manager);

    Manager getManagerById(Long managerId);

    Page<Manager> getAllManagers(Pageable pageable);

    Manager setManagerSubscription(Long managerId, Long subscriptionId);
}
