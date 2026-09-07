package com.example.movieBooking.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidBookingYearException.class)
    public ResponseEntity<?> handleInvalidBookingYearException(InvalidBookingYearException ex) {
        return ResponseEntity.ok(Map.of(
                "success", false,
                "message", ex.getMessage()
        ));
    }
}