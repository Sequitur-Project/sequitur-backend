package com.sequitur.api.Subscriptions.domain.model;

import com.sequitur.api.DataCollection.domain.model.StudentMessage;
import com.sequitur.api.IdentityAccessManagement.domain.model.Manager;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.engine.spi.Managed;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
    private List<Payment> payments;

    private Double price;

    @Column(length = 1000)
    private String description;
    private String title;
}
