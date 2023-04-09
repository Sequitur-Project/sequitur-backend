package com.sequitur.api.IdentityAccessManagement.resource;

import com.sequitur.api.IdentityAccessManagement.domain.model.UserModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SaveManagerResource extends UserModel {

    @Size(max = 25)
    private String firstName;


    @Size(max = 25)
    private String lastName;


    @Size(max = 25)
    private String email;


    @Size(max = 15)
    private String password;


    @Size(max = 9)
    private String telephone;

    private SaveUniversityResource university;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public SaveUniversityResource getUniversity() {
        return university;
    }

    public void setUniversity(SaveUniversityResource university) {
        this.university = university;
    }

}
