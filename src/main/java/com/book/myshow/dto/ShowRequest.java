package com.book.myshow.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowRequest {
    @NotNull(message = "Show date is required")
    @Future(message = "Show date must be in the future")
    private LocalDate showDate;
    
    @NotNull(message = "Show time is required")
    private LocalTime showTime;
    
    @NotNull(message = "Theater ID is required")
    private Long theaterId;
    
    @NotNull(message = "Movie ID is required")
    private Long movieId;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Ticket price must be greater than 0")
    private BigDecimal ticketPrice;

}
