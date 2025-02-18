package com.tsg.posts.models.request;


import com.tsg.commons.models.enums.PostStatus;
import lombok.Data;

@Data
public class PostDTO {

    private String title;
    private String content;
    private PostStatus status;


}
