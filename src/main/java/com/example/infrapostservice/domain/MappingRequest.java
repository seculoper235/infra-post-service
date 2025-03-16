package com.example.infrapostservice.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize
public record MappingRequest(
        String mappedBy,
        List<String> files
) {
}
