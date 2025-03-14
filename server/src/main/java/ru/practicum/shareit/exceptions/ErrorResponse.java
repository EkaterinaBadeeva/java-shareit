package ru.practicum.shareit.exceptions;

import lombok.Getter;

@Getter
class ErrorResponse {
    private final String error;
    private final String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }
}