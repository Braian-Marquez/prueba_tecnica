package com.tsg.authentication.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsg.commons.models.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
 

    
}
