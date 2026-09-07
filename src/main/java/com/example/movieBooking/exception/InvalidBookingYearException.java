package com.example.movieBooking.exception;

public class InvalidBookingYearException extends RuntimeException {
    public InvalidBookingYearException(String message) {
        super(message);
    }
}