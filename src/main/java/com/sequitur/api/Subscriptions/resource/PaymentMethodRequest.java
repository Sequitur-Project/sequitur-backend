package com.sequitur.api.Subscriptions.resource;

import lombok.Data;

@Data
public class PaymentMethodRequest {
    //private String paymentMethodId;
    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;
    private String cvc;
}
