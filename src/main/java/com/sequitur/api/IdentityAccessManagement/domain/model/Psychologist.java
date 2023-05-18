package com.sequitur.api.IdentityAccessManagement.domain.model;

import com.sequitur.api.ProactiveCommunication.domain.model.Appointment;
import com.sequitur.api.ProactiveCommunication.domain.model.Notification;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Data
@Entity
@Table(name = "psychologists")
public class Psychologist extends UserModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "university_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private University university;

    @OneToMany(mappedBy = "psychologist", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }
}
