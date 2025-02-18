package com.tsg.authentication.models.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tsg.authentication.models.response.UserListResponse;
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
                        "FROM users u " +
                        "LEFT JOIN customer c ON u.id = c.id_user " +
                        "LEFT JOIN users_roles ur ON ur.user_entity_id = u.id " +
                        "LEFT JOIN roles r ON r.id = ur.roles_id " +
                        "WHERE u.soft_delete = false AND u.email = :email " + 
                        "GROUP BY u.id, c.id, c.first_name", nativeQuery = true)
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
                        "FROM users u " +
                        "LEFT JOIN customer c ON u.id = c.id_user " +
                        "LEFT JOIN users_roles AS ur ON ur.user_entity_id = u.id " +
                        "LEFT JOIN roles AS r ON r.id = ur.roles_id " +
                        "WHERE u.id = :userId AND u.soft_delete = false " +
                        "GROUP BY u.id, c.id, c.first_name", nativeQuery = true)
        Optional<UserProfileResponse> findUserProfileByUserId(@Param("userId") Long userId);

        @Query(value = "SELECT " +
                        "u.id AS idUser, " +
                        "c.first_name AS name, " +
                        "u.email AS email, " +
                        "ARRAY_AGG(DISTINCT r.name) AS roles " +
                        "FROM users u " +
                        "LEFT JOIN customer c ON u.id = c.id_user " +
                        "LEFT JOIN users_roles ur ON ur.user_entity_id = u.id " +
                        "LEFT JOIN roles r ON r.id = ur.roles_id " +
                        "WHERE u.soft_delete = false " +
                        "GROUP BY u.id, c.id, c.first_name " +
                        "ORDER BY " +
                        "CASE WHEN :sortDirection = 'ASC' THEN u.id ELSE NULL END ASC, " +
                        "CASE WHEN :sortDirection = 'DESC' THEN u.id ELSE NULL END DESC " +
                        "LIMIT :pageSize OFFSET :offset", nativeQuery = true)
        List<UserListResponse> findUserPage(String sortDirection, int pageSize, int offset);

        @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
                        "FROM users u WHERE u.email = :email ", nativeQuery = true)
        boolean existsByEmailAndIdNot(@Param("email") String email);

}
