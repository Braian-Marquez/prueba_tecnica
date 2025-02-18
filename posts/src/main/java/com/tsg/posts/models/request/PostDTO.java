package com.tsg.posts.models.request;

import com.tsg.commons.models.enums.PostStatus;
import com.tsg.posts.utils.ValidPostStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostDTO {

    @NotNull(message = "El title no puede ser nulo")
    @Size(min = 2, message = "El title debe tener al menos 2 caracteres")
    private String title;
    @NotNull(message = "El content no puede ser nulo")
    @Size(min = 2, message = "El content debe tener al menos 2 caracteres")
    private String content;
    
    @ValidPostStatus
    private PostStatus status;

}
