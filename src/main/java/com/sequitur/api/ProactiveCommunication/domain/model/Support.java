package com.sequitur.api.ProactiveCommunication.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "supports")
@Data
public class Support {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String issue;
    private String message;
}
