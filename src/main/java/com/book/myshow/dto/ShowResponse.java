package com.book.myshow.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.book.myshow.entity.Show;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowResponse {

	private Long showId;
    private LocalDate showDate;
    private LocalTime showTime;
    private String theaterName;
    private String movieName;
    private BigDecimal ticketPrice;
    private LocalDateTime createdAt;

    public ShowResponse(Show show) {
        this.showId = show.getShowId();
        this.showDate = show.getShowDate();
        this.showTime = show.getShowTime();
        this.theaterName = show.getTheater().getTheaterName();
        this.movieName = show.getMovie().getMovieName();
        this.ticketPrice = show.getTicketPrice();
        this.createdAt = show.getCreatedAt();
    }
}
