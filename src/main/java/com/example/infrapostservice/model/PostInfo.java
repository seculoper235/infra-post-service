package com.example.infrapostservice.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@JsonSerialize
public record PostInfo(
        UUID id,
        String title,
        Optional<String> thumbnail,
        String summary,
        Instant createdAt
) {
}
