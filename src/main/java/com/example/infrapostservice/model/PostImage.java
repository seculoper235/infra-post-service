package com.example.infrapostservice.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record PostImage(
        String uuid,
        String path
) {
}
