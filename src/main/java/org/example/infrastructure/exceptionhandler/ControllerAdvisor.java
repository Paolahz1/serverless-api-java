package org.example.infrastructure.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.exception.ConflictException;
import org.example.domain.exception.NotFoundException;

import java.util.Map;

public class ControllerAdvisor {

    public static final ObjectMapper mapper = new ObjectMapper();

    public static Map<String, Object> handle(Exception ex){


        if(ex instanceof NotFoundException){
            return response( ((NotFoundException) ex).getError());
        }

        if (ex instanceof ConflictException){
            return response(((ConflictException) ex).getError());
        }

        ErrorResponse errorResponse = new ErrorResponse( 500, ExceptionResponse.GENERAL_ERROR, Map.of());
        return response(errorResponse);

    }


    private static Map<String, Object> response(ErrorResponse body) {
        try {
            return Map.of(
                    "statusCode", body.getStatus(),
                    "body", mapper.writeValueAsString(body),
                    "headers", Map.of("Content-Type", "application/json")
            );
        } catch (Exception e) {
            return Map.of(
                    "statusCode", 500,
                    "body", "{\"message\":\"JSON serialization error\"}"
            );
        }
    }
}
