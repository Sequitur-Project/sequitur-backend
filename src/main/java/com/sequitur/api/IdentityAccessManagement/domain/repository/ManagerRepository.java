package com.sequitur.api.IdentityAccessManagement.domain.repository;

import com.sequitur.api.IdentityAccessManagement.domain.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
