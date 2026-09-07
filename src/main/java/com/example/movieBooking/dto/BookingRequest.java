package com.example.movieBooking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @NotBlank(message = "Customer name cannot be empty")
    @Size(min = 2, message = "Customer name must have at least 2 characters")
    private String customerName;

    @NotBlank(message = "Movie name cannot be empty")
    private String movieName;

    @NotBlank(message = "Show date cannot be empty")
    private String showDate;

    @NotBlank(message = "Show time cannot be empty")
    private String showTime;

    @NotBlank(message = "Seat number cannot be empty")
    private String seatNumber;
}