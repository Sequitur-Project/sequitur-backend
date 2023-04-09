package com.sequitur.api.IdentityAccessManagement.resource;

import com.sequitur.api.IdentityAccessManagement.domain.model.University;
import com.sequitur.api.IdentityAccessManagement.domain.model.UserModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ManagerResource extends UserModel {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String telephone;

    private UniversityResource university;
}
