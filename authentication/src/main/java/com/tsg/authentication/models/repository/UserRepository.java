package com.tsg.authentication.models.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tsg.authentication.models.response.UserProfileResponse;
import com.tsg.commons.models.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.email=?1")
    Optional<UserEntity> findByUsername(String username);

    @Query(value = "SELECT " +
            "c.id AS idProfile, " +
            "u.id AS idUser, " +
            "c.first_name AS name, " +
            "ARRAY_AGG(DISTINCT r.name) AS roles " +
            "FROM usuario u " +
            "LEFT JOIN customer c ON u.id = c.id_user " +
            "LEFT JOIN usuario_roles AS ur ON ur.user_entity_id = u.id " +
            "LEFT JOIN roles AS r ON r.id = ur.roles_id " +
            "WHERE u.email = :email " +
            "GROUP BY u.id, c.id,  c.first_name", nativeQuery = true)
    Optional<UserProfileResponse> findUserProfileByUsername(@Param("email") String email);

    @Query("SELECT u FROM UserEntity u WHERE u.email=?1 OR u.username=?2")
    Optional<UserEntity> findByEmailOrUsername(String email, String username);

    @Query("SELECT u FROM UserEntity u WHERE u.email=?1")
    Optional<UserEntity> findByEmail(String email);

    @Query(value = "SELECT " +
            "c.id AS idProfile, " +
            "u.id AS idUser, " +
            "c.first_name AS name, " +
            "ARRAY_AGG(DISTINCT r.name) AS roles " +
            "FROM usuario u " +
            "LEFT JOIN customer c ON u.id = c.id_user " +
            "LEFT JOIN usuario_roles AS ur ON ur.user_entity_id = u.id " +
            "LEFT JOIN roles AS r ON r.id = ur.roles_id " +
            "WHERE u.id = :userId " +
            "GROUP BY u.id, c.id, c.first_name", nativeQuery = true)
    Optional<UserProfileResponse> findUserProfileByUserId(@Param("userId") Long userId);

}
