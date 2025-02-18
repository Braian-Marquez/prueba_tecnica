package com.tsg.authentication.models.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUser {
    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
    private String firstName;
    
    @NotNull(message = "El apellido no puede ser nulo")
    @Size(min = 2, message = "El apellido debe tener al menos 2 caracteres")
    private String lastName;
  
}