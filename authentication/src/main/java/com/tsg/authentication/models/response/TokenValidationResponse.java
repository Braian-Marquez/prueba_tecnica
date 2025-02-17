package com.tsg.authentication.models.response;

import java.util.List;

import lombok.Data;
@Data
public class TokenValidationResponse {
	private List<String> roles;
	private Long idProfile;
}
