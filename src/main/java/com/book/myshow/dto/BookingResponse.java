package com.book.myshow.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingResponse {
    
    private Long bookingId;
    private String bookingStatus;
    private LocalDateTime bookingDate;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private BigDecimal totalAmount;
    private ShowDetails showDetails;
    private List<SeatDetails> bookedSeats;
    private String message;
    
    // Nested classes for response structure
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ShowDetails {
        private Long showId;
        private String movieName;
        private String theaterName;
        private LocalDate showDate;
        private LocalTime showTime;
        private BigDecimal ticketPrice;
        
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SeatDetails {
        private String seatName;
        
    }
}
