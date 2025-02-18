package com.tsg.authentication.models.response;

import lombok.Data;

@Data
public class CustomerResponse {
    private Long idCustomer;
    private String name;
    private String lastName;
    private String email;
}
