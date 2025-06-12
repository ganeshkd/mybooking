package com.book.myshow.dto;

import java.util.List;

import com.book.myshow.entity.Theater;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TheaterShowResponse {

	private Long theaterId;
    private String theaterName;
    private String locationUrl;
    private List<ShowResponse> shows;

    public TheaterShowResponse(Theater theater, List<ShowResponse> shows) {
        this.theaterId = theater.getTheaterId();
        this.theaterName = theater.getTheaterName();
        this.locationUrl = theater.getLocationUrl();
        this.shows = shows;
    }
}
