package com.book.myshow.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequest {
    
    @NotBlank(message = "Theater name is required")
    private String theaterName;
    
    @NotNull(message = "Show date is required")
    private LocalDate showDate;
    
    @NotNull(message = "Show time is required")
    private LocalTime showTime;
    
    @NotEmpty(message = "At least one preferred seat is required")
    private List<String> preferredSeats;
    
    @NotBlank(message = "Customer name is required")
    @Size(max = 100, message = "Customer name must not exceed 100 characters")
    private String customerName;
    
    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String customerEmail;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{0,3}-?\\d{6,14}$", message = "Invalid phone number format")
    private String customerPhone;
    
}
