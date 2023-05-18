package com.sequitur.api.IdentityAccessManagement.domain.model;

import com.sequitur.api.DataCollection.domain.model.StudentMessage;
import com.sequitur.api.Subscriptions.domain.model.Payment;
import com.sequitur.api.Subscriptions.domain.model.Subscription;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "managers")
@Data
public class Manager extends UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "university_id", referencedColumnName = "id")
    private University university;

    @OneToOne(mappedBy = "manager", cascade = CascadeType.ALL)
    private Subscription subscription;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private List<Payment> payments;

    private boolean isSubscribed;

    private String role;

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
