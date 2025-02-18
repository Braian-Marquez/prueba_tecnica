package com.tsg.posts.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

import com.tsg.commons.models.enums.PostStatus;

public class PostStatusValidator implements ConstraintValidator<ValidPostStatus, PostStatus> {

    private static final List<PostStatus> ALLOWED_STATUSES = Arrays.asList(PostStatus.DRAFT, PostStatus.PUBLISHED, PostStatus.ARCHIVED);

    @Override
    public boolean isValid(PostStatus status, ConstraintValidatorContext context) {
        return status != null && ALLOWED_STATUSES.contains(status);
    }
}
