package com.example.movieBooking.service.impl;

import com.example.movieBooking.dto.BookingRequest;
import com.example.movieBooking.dto.BookingResponse;
import com.example.movieBooking.model.Booking;
import com.example.movieBooking.repository.BookingRepository;
import com.example.movieBooking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public BookingResponse createBooking(BookingRequest request) {
        Booking booking = new Booking();
        booking.setCustomerName(request.getCustomerName());
        booking.setMovieName(request.getMovieName());
        booking.setShowDate(request.getShowDate());
        booking.setShowTime(request.getShowTime());
        booking.setSeatNumber(request.getSeatNumber());

        Booking savedBooking = bookingRepository.save(booking);
        return mapToResponse(savedBooking);
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponse updateBooking(Long id, BookingRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        booking.setCustomerName(request.getCustomerName());
        booking.setMovieName(request.getMovieName());
        booking.setShowDate(request.getShowDate());
        booking.setShowTime(request.getShowTime());
        booking.setSeatNumber(request.getSeatNumber());

        Booking updatedBooking = bookingRepository.save(booking);
        return mapToResponse(updatedBooking);
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setCustomerName(booking.getCustomerName());
        response.setMovieName(booking.getMovieName());
        response.setShowDate(booking.getShowDate());
        response.setShowTime(booking.getShowTime());
        response.setSeatNumber(booking.getSeatNumber());
        return response;
    }
}