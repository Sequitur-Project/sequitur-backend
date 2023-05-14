package com.sequitur.api.Subscriptions.domain.service;

import com.sequitur.api.Subscriptions.domain.model.Payment;

public interface PaymentService {
    Payment findByPaymentId(String paymentId);
}
