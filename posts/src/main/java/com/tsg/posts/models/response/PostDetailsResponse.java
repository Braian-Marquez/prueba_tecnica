package com.tsg.posts.models.response;

import lombok.Data;

@Data
public class PostDetailsResponse {
    private Long id;
    private String title;
    private String content;
    private String status;
    private String createdAt;
    private String updatedAt;
}
