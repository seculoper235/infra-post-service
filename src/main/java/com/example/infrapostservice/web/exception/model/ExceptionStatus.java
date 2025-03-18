package com.example.infrapostservice.web.exception.model;

import lombok.Getter;

@Getter
public enum ExceptionStatus {
    PS001("Entity Not Found"),
    PS002("External Service Exception"),
    PS003("Invalid Argument Exception");

    private final String label;

    ExceptionStatus(String label) {
        this.label = label;
    }
}
