package org.example.infrastructure.exceptionhandler;

import java.time.Instant;
import java.util.Map;

public class ErrorResponse {

    private final int status;
    private final String message;
    private final Instant timestamp;
    private final Map<String, Object> additionalInfo;

    public ErrorResponse(int status, ExceptionResponse exceptionResponse,Map<String, Object> additionalInfo) {
        this.status = status;
        this.message = exceptionResponse.getMessage();
        this.timestamp = Instant.now();
        this.additionalInfo = additionalInfo;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }
}
