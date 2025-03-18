package com.example.infrapostservice.web;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

@JsonSerialize
public record PostCreateRequest(
        String title,
        @Size(max = 100, message = "summary는 100 이하여야 합니다.")
        String summary,
        String contents,
        List<UUID> images
) {
}
