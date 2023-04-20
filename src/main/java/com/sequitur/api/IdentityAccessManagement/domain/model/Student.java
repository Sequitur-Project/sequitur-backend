package com.sequitur.api.IdentityAccessManagement.domain.model;

import com.sequitur.api.DataCollection.domain.model.Conversation;
import com.sequitur.api.DataCollection.domain.model.Binnacle;
import com.sequitur.api.ProactiveCommunication.domain.model.Appointment;
import com.sequitur.api.ProactiveCommunication.domain.model.Notification;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "students")
public class Student extends UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "university_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private University university;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private Conversation conversation;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private Binnacle binnacle;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public Binnacle getBinnacle() {
        return binnacle;
    }

    public void setBinnacle(Binnacle binnacle) {
        this.binnacle = binnacle;
    }
}
