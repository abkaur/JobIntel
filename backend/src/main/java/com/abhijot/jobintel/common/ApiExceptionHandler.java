package com.abhijot.jobintel.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@RestControllerAdvice
public class ApiExceptionHandler {

    record ApiError(
            Instant timestamp,
            int status,
            String error,
            String message,
            String path
    ) {}

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus
    public ApiError handleResponseStatus(ResponseStatusException ex, HttpServletRequest req) {
        HttpStatus status = (HttpStatus) ex.getStatusCode();
        return new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getReason(),
                req.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleOther(Exception ex, HttpServletRequest req) {
        return new ApiError(
                Instant.now(),
                500,
                "Internal Server Error",
                "Something went wrong",
                req.getRequestURI()
        );
    }
}
