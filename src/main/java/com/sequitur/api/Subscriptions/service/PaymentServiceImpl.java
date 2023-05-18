package com.sequitur.api.Subscriptions.service;

import com.sequitur.api.Subscriptions.domain.model.Payment;
import com.sequitur.api.Subscriptions.domain.repository.PaymentRepository;
import com.sequitur.api.Subscriptions.domain.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment findByPaymentId(String paymentId) {
        return paymentRepository.findByPaymentId(paymentId);
    }
}
