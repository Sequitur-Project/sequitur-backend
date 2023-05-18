package com.sequitur.api.DataCollection.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sequitur.api.SharedContext.domain.model.AuditModel;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@Table(name = "binnacle_entries")
public class BinnacleEntry extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "binnacle_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Binnacle binnacle;

    private String emoji;

    private String feeling;

    private String reason;

    private String extraText;
}
