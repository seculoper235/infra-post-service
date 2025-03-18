package com.example.infrapostservice.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vavr.control.Option;

import java.time.Instant;
import java.util.UUID;

@JsonSerialize
public record PostInfo(
        UUID id,
        String title,
        Option<String> thumbnail,
        String summary,
        Instant createdAt
) {
}
