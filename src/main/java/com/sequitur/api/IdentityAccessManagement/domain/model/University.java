package com.sequitur.api.IdentityAccessManagement.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "universities")
@Data
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Size(max = 200)
    private String name;


    @Size(max = 25)
    private String country;


    @Size(max = 25)
    private String city;


    @Size(max = 100)
    private String address;


    @Size(max = 25)
    private String zipCode;

    @Size(max = 11)
    private String ruc;

    @OneToOne(mappedBy = "university")
    private Manager manager;

    @OneToMany(mappedBy = "university")
    private List<Student> students;

    @OneToMany(mappedBy = "university")
    private List<Psychologist> psychologists;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Psychologist> getPsychologists() {
        return psychologists;
    }

    public void setPsychologists(List<Psychologist> psychologists) {
        this.psychologists = psychologists;
    }
}
