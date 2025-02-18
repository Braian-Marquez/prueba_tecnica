package com.tsg.authentication.models.response;

import java.util.List;

public interface UserListResponse {
    Long getIdCustomer();
    Long getIdUser();
    String getName();
    String getEmail();
    List<String> getRoles();

}

