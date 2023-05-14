package com.sequitur.api.Subscriptions.resource;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class SaveSubscriptionResource {

    private Double price;

    @Column(length = 1000)
    private String description;

    private String title;
}
