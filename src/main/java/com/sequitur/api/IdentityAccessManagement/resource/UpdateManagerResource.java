package com.sequitur.api.IdentityAccessManagement.resource;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateManagerResource {
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


}
