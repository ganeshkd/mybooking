package com.book.myshow.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.book.myshow.dto.BookingRequest;
import com.book.myshow.dto.BookingResponse;
import com.book.myshow.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
	
	@Autowired
	private BookingService bookingService;

    /**
     * Book movie tickets
     * POST /api/v1/bookings
     */
    @PostMapping
    public ResponseEntity<BookingResponse> bookTickets(@Valid @RequestBody BookingRequest request) {
        try {
            BookingResponse response = bookingService.bookTickets(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            BookingResponse errorResponse = new BookingResponse();
            errorResponse.setMessage("Booking failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    /**
     * Get booking details by ID
     * GET /api/v1/bookings/{bookingId}
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingDetails(@PathVariable Long bookingId) {
        try {
            BookingResponse response = bookingService.getBookingDetails(bookingId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BookingResponse errorResponse = new BookingResponse();
            errorResponse.setMessage("Error retrieving booking: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
    
    /**
     * Cancel booking
     * DELETE /api/v1/bookings/{bookingId}
     */
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Map<String, String>> cancelBooking(@PathVariable Long bookingId) {
        try {
            bookingService.cancelBooking(bookingId);
            return ResponseEntity.ok(Map.of(
                "message", "Booking cancelled successfully",
                "bookingId", bookingId.toString()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Cancellation failed: " + e.getMessage()));
        }
    }
}
