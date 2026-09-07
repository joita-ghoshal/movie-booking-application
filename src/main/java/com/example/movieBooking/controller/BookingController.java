package com.example.movieBooking.controller;

import com.example.movieBooking.exception.InvalidBookingYearException;
import com.example.movieBooking.model.Booking;
import com.example.movieBooking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"}, allowCredentials = "true")
public class BookingController {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // 1. Get all tickets
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // 2. Book new ticket with Year Exception Check
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        validateShowYear(booking.getShowDate());

        Booking saved = bookingRepository.save(booking);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Ticket booked successfully!",
                "data", saved
        ));
    }

    // 3. Initiate edit window (tracks server time & checks max 3 edits)
    @PostMapping("/{id}/start-edit")
    public ResponseEntity<?> startEditSession(@PathVariable Long id) {
        return bookingRepository.findById(id).map(booking -> {
            if (booking.getEditCount() >= 3) {
                return ResponseEntity.ok(Map.of(
                        "allowed", false,
                        "message", "Maximum edit limit reached! You cannot edit this ticket more than 3 times."
                ));
            }

            booking.setEditStartTime(LocalDateTime.now());
            bookingRepository.save(booking);

            return ResponseEntity.ok(Map.of(
                    "allowed", true,
                    "message", "Edit window started. You have 2 minutes to submit."
            ));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 4. Update ticket with 2-minute, 3-edit & Year Exception validation
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody Booking updatedData) {
        validateShowYear(updatedData.getShowDate());

        return bookingRepository.findById(id).map(booking -> {
            if (booking.getEditCount() >= 3) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Update rejected: Maximum edit limit reached (3 times)!"
                ));
            }

            if (booking.getEditStartTime() != null) {
                long secondsPassed = Duration.between(booking.getEditStartTime(), LocalDateTime.now()).getSeconds();
                if (secondsPassed > 120) {
                    booking.setEditStartTime(null);
                    bookingRepository.save(booking);
                    return ResponseEntity.ok(Map.of(
                            "success", false,
                            "message", "Time over! 2-minute edit window has expired. Ticket was not updated."
                    ));
                }
            }

            booking.setCustomerName(updatedData.getCustomerName());
            booking.setMovieName(updatedData.getMovieName());
            booking.setShowDate(updatedData.getShowDate());
            booking.setShowTime(updatedData.getShowTime());
            booking.setSeatNumber(updatedData.getSeatNumber());

            booking.setEditCount(booking.getEditCount() + 1);
            booking.setEditStartTime(null);

            bookingRepository.save(booking);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Ticket updated successfully!"
            ));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 5. Delete ticket
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Helper method: throws Exception if year > 2027
    private void validateShowYear(String showDate) {
        if (showDate != null && !showDate.isEmpty()) {
            try {
                LocalDate date = LocalDate.parse(showDate);
                if (date.getYear() > 2027) {
                    throw new InvalidBookingYearException("Exception: Booking is only allowed up to year 2027! Bookings for year 2028 or later are not permitted.");
                }
            } catch (Exception e) {
                if (e instanceof InvalidBookingYearException) {
                    throw e;
                }
            }




        }
    }
}