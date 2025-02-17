package com.tsg.authentication.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticationRequest {
	
    @Email()
    private String email;
    private String username;
    @Size(min = 8)
    private String password;
}