package com.sequitur.api.IdentityAccessManagement.resource;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UniversityResource {
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
}
