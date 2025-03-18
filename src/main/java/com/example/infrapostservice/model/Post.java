package com.example.infrapostservice.model;

import com.example.infrapostservice.infra.PostImageEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vavr.control.Option;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@JsonSerialize
public record Post(
        UUID id,
        String title,
        Option<String> thumbnail,
        String contents,
        List<PostImageEntity> images,
        Instant createdAt,
        Instant updatedAt
) {
}
