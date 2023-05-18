package com.sequitur.api.Subscriptions.resource;

import lombok.Data;

@Data
public class ConfirmPaymentRequest {
    private String paymentId;
    private String paymentMethodId; // new field
    private String paymentMethodToken; // new field
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
