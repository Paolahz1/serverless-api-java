package org.example.infrastructure.exceptionhandler;

public enum ExceptionResponse {
    USER_NOT_FOUND("No user records found"),
    CONFLICT_USER("User id already exists"),
    BAD_USER("identification, name, and email must not be null"),
    GENERAL_ERROR(" Default Internal Server Error");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
