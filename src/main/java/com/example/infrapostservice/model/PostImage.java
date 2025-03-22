package com.example.infrapostservice.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.UUID;

@JsonSerialize
public record PostImage(
        UUID id,
        String name,
        String path
) {
}
