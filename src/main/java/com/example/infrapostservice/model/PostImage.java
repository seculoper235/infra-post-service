package com.example.infrapostservice.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.UUID;

@JsonSerialize
public record PostImage(
        UUID uuid,
        String path
) {
}
