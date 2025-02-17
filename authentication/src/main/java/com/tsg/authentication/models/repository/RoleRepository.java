package com.tsg.authentication.models.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tsg.commons.models.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
