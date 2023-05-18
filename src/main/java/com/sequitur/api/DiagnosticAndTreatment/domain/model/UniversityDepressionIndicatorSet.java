package com.sequitur.api.DiagnosticAndTreatment.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sequitur.api.IdentityAccessManagement.domain.model.University;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "depression_indicators")
public class UniversityDepressionIndicatorSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "university_id", referencedColumnName = "id")
    private University university;

    @Column(name = "depression_percentage")
    private double depressionPercentage;

    @Column(name = "no_depression_percentage")
    private double noDepressionPercentage;

    private int studentsQuantity;
    private int psychologistsQuantity;
    private int depressionLeveQuantity;
    private int depressionMinimaQuantity;
    private int depressionModeradaQuantity;
    private int depressionModeradaSeveraQuantity;
    private int depressionSeveraQuantity;

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

    public double getDepressionPercentage() {
        return depressionPercentage;
    }

    public void setDepressionPercentage(double depressionPercentage) {
        this.depressionPercentage = depressionPercentage;
    }

    public double getNoDepressionPercentage() {
        return noDepressionPercentage;
    }

    public void setNoDepressionPercentage(double noDepressionPercentage) {
        this.noDepressionPercentage = noDepressionPercentage;
    }

    public int getStudentsQuantity() {
        return studentsQuantity;
    }

    public void setStudentsQuantity(int studentsQuantity) {
        this.studentsQuantity = studentsQuantity;
    }
}
