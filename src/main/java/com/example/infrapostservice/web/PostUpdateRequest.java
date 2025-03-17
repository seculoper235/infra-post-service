package com.example.infrapostservice.web;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
import java.util.UUID;

@JsonSerialize
public record PostUpdateRequest(
        UUID id,
        String title,
        String contents,
        List<String> images
) {
}
