package com.tsg.authentication.models.request;

import jakarta.validation.constraints.*;
import lombok.Data;
@Data
public class CustomerRequest {

	@NotBlank(message = "Email can not be empty.")
	@Email(message = "Email is not valid.")
	private String email;

	@Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
	@Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "First name must not contain numbers or special characters.")
	private String firstName;

	@Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
	@Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "Last name must not contain numbers or special characters.")
	private String lastName;
	
}
