package com.sequitur.api.Subscriptions.resource;

import lombok.Data;

@Data
public class SubscriptionResource {
    private Long id;

    private Integer price;

    private String description;

    private String type;
}
