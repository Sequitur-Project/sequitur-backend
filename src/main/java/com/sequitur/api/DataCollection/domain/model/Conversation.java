package com.sequitur.api.DataCollection.domain.model;

import com.google.cloud.dialogflow.v2.FaqAnswer;
import com.sequitur.api.DiagnosticAndTreatment.domain.model.Result;
import com.sequitur.api.IdentityAccessManagement.domain.model.Student;
import com.sequitur.api.SharedContext.domain.model.AuditModel;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "conversations")
public class Conversation extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Student student;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<StudentMessage> studentMessages;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<BotMessage> botMessages;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<Result> results;

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


    public List<StudentMessage> getStudentMessages() {
        return studentMessages;
    }

    public void setStudentMessages(List<StudentMessage> studentMessages) {
        this.studentMessages = studentMessages;
    }

    public List<BotMessage> getBotMessages() {
        return botMessages;
    }

    public void setBotMessages(List<BotMessage> botMessages) {
        this.botMessages = botMessages;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
