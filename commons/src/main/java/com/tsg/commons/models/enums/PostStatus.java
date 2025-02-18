package com.tsg.commons.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

public enum PostStatus {
    DRAFT, PUBLISHED, ARCHIVED;

    @JsonCreator
    public static PostStatus fromString(String value) {
        return Stream.of(PostStatus.values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "El status solo puede ser DRAFT, PUBLISHED o ARCHIVED, recibido: " + value));
    }

    @JsonValue
    public String getValue() {
        return name();
    }
}

