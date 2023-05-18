package com.sequitur.api.DataCollection.domain.model;

import com.sequitur.api.IdentityAccessManagement.domain.model.Student;
import com.sequitur.api.SharedContext.domain.model.AuditModel;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Data
@Table(name = "binnacles")
public class Binnacle extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Student student;

    @OneToMany(mappedBy = "binnacle", cascade = CascadeType.ALL)
    private List<BinnacleEntry> binnacleEntries;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
