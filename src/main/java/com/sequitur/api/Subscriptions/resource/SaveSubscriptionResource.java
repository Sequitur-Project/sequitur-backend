package com.sequitur.api.Subscriptions.resource;

import lombok.Data;

@Data
public class SaveSubscriptionResource {

    private Integer price;

    private String description;

    private String type;
}
