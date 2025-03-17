package com.example.infrapostservice.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.Instant;
import java.util.List;

@JsonSerialize
public record PostDetail(
        String id,
        String title,
        String contents,
        List<PostImage> images,
        Instant createdAt
) {
}
