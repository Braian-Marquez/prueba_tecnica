package com.tsg.authentication.models.response;

import java.util.List;

public interface UserListResponse {

    Long getIdUser();
    String getName();
    String getEmail();
    List<String> getRoles();

}

