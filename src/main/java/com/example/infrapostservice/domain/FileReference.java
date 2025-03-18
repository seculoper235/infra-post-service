package com.example.infrapostservice.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.UUID;

@JsonSerialize
public record FileReference(
        UUID id,
        String name,
        String path
) {
}
