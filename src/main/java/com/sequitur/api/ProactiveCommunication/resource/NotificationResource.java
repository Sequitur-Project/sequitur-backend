package com.sequitur.api.ProactiveCommunication.resource;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class NotificationResource {

    private Long id;
    private String title;
    private String message;
}
