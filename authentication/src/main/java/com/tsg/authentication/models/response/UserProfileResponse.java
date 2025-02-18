package com.tsg.authentication.models.response;

import java.util.List;

public interface UserProfileResponse {
	 Long getIdCustomer();
	 Long getIdUser();
	 String getToken();
	 String getName();
	 List<String> getRoles();
}
