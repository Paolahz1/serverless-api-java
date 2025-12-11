package org.example.domain.exception;


import org.example.infrastructure.exceptionhandler.ErrorResponse;
import org.example.infrastructure.exceptionhandler.ExceptionResponse;

import java.util.Map;

public class ConflictException extends RuntimeException{
    private final ErrorResponse error;

    public ConflictException(ExceptionResponse message, Map<String, Object> additionalInfo) {
        this.error =  new ErrorResponse(409, message, additionalInfo);
    }

    public ErrorResponse getError() { return error; }
}

