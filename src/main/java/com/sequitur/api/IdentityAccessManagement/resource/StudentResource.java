package com.sequitur.api.IdentityAccessManagement.resource;

import com.sequitur.api.IdentityAccessManagement.domain.model.UserModel;
import lombok.Data;

import java.util.Date;

@Data
public class StudentResource extends UserModel {

    private Long id;
    private Long universityId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String telephone;
    private Date birthDate;
}
