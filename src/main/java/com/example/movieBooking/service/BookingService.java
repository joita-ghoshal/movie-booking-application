package com.example.movieBooking.service;

import com.example.movieBooking.dto.BookingRequest;
import com.example.movieBooking.dto.BookingResponse;
import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request);
    List<BookingResponse> getAllBookings();
    BookingResponse updateBooking(Long id, BookingRequest request);
    void deleteBooking(Long id);
}