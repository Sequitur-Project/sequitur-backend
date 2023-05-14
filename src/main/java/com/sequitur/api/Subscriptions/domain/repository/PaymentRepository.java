package com.sequitur.api.Subscriptions.domain.repository;

import com.sequitur.api.Subscriptions.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Payment findByPaymentId(String paymentId);
}
