package com.example.infrapostservice.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vavr.control.Option;

import java.time.Instant;

@JsonSerialize
public record PostInfo(
        String id,
        String title,
        Option<String> thumbnail,
        String summary,
        Instant createdAt
) {
}
