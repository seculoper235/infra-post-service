package com.example.infrapostservice.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@JsonSerialize
public record PostDetail(
        UUID id,
        String title,
        String contents,
        List<PostImage> images,
        Instant createdAt
) {
}
