package com.tsg.posts.models.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tsg.commons.models.entity.Post;
import com.tsg.posts.models.response.PostResponse;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT " +
            "p.id AS id, " +
            "p.title AS title, " +
            "p.content AS content, " +
            "p.status AS status, " +
            "p.created_at AS created_at, " +
            "p.updated_at AS updated_at " +
            "FROM posts p " +
            "WHERE p.soft_delete = false " +
            "ORDER BY " +
            "CASE WHEN :sortDirection = 'ASC' THEN p.id END ASC, " +
            "CASE WHEN :sortDirection = 'DESC' THEN p.id END DESC " +
            "LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<PostResponse> findPostPage(
            @Param("sortDirection") String sortDirection,
            @Param("pageSize") int pageSize,
            @Param("offset") int offset);

}
