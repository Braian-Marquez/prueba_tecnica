package com.tsg.authentication.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotNull(message = "El email no puede ser nulo")
    @Email(message = "El email no tiene un formato válido")
    private String email;
    
    @NotNull(message = "La contraseña no puede ser nula")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;
}
