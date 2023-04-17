package com.sequitur.api.IdentityAccessManagement.resource;

import com.sequitur.api.IdentityAccessManagement.domain.model.UserModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class SaveStudentResource extends UserModel {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String telephone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
}
