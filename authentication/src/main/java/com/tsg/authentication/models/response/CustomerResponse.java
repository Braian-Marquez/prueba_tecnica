package com.tsg.authentication.models.response;

import lombok.Data;

@Data
public class CustomerResponse {
    private Long idCustomer;
    private Long idUser;
    private String name;
    private String lastName;
    private String email;
}
