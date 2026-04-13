package com.io.kairo.enums;

import lombok.Getter;

@Getter
public enum TaskStatus {

    TODO("TODO"),
    PENDING("PENDING"),
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED");

    private final String status;

    TaskStatus(String status) {
        this.status = status;
    }
}
