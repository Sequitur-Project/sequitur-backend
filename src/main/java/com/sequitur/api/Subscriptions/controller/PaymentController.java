package com.sequitur.api.Subscriptions.controller;

import com.sequitur.api.Subscriptions.domain.model.Payment;
import com.sequitur.api.Subscriptions.domain.model.Subscription;
import com.sequitur.api.Subscriptions.domain.service.PaymentService;
import com.sequitur.api.Subscriptions.resource.PaymentResource;
import com.sequitur.api.Subscriptions.resource.SubscriptionResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "payments", description = "Payments API")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ModelMapper mapper;

    @Operation(summary = "Get Payment by PaymentId", description = "Get a Payment by specifying Id", tags = { "payments" })
    @GetMapping("/payments/{id}")
    public PaymentResource getPaymentById(
            @Parameter(description="Payment Id")
            @PathVariable(name = "id") String paymentId) {
        return convertToResource(paymentService.findByPaymentId(paymentId));
    }

    private PaymentResource convertToResource(Payment entity) {
        return mapper.map(entity, PaymentResource.class);
    }
}
