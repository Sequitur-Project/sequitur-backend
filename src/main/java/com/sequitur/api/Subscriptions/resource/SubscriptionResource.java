package com.sequitur.api.Subscriptions.resource;

import lombok.Data;

@Data
public class SubscriptionResource {
    private Long id;

    private Double price;

    private String description;

    private String title;
}
