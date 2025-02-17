package com.tsg.authentication.models.response;

import lombok.Data;

@Data
public class UserResponse {
	private Long idUser;
	private Long idProfile;
	private String token;
}
