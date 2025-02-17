package com.tsg.authentication.models.response;

import java.util.List;

public class UserProfileResponseImpl implements UserProfileResponse {
    private Long idProfile;
    private Long idUser;
    private String name;
    private List<String> roles;
    private String token; 

    public UserProfileResponseImpl(Long idProfile, Long idUser, String name, List<String> roles, String token) {
        this.idProfile = idProfile;
        this.idUser = idUser;
        this.name = name;
        this.roles = roles;
        this.token = token;
    }

    @Override
    public Long getIdProfile() {
        return idProfile;
    }

    @Override
    public Long getIdUser() {
        return idUser;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getRoles() {
        return roles;
    }
}
