package com.sequitur.api.IdentityAccessManagement.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sequitur.api.DiagnosticAndTreatment.domain.model.Result;
import com.sequitur.api.IdentityAccessManagement.domain.model.UserModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StudentResource extends UserModel {

    private Long id;
    private Long universityId;
    private String firstName;
    private String lastName;
    private String genre;
    private String email;
    private String password;
    private String telephone;
    private Date birthDate;
    @JsonManagedReference
    private List<Result> results;
}
