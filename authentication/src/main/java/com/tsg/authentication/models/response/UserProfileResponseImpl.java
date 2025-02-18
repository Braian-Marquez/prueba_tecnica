package com.tsg.authentication.models.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseImpl implements UserProfileResponse {
    private Long idCustomer;
    private Long idUser;
    private String name;
    private List<String> roles;
    private String token; 
}
