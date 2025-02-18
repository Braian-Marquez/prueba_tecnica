package com.tsg.posts.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PostStatusValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPostStatus {

    String message() default "El status solo puede ser DRAFT, PUBLISHED o ARCHIVED";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

