package com.sequitur.api.Subscriptions.resource;

import lombok.Data;

@Data
public class PaymentResource {
    private Long id;

    private String paymentId;

    private String currency;

    private String description;

    private Double amount;
}
