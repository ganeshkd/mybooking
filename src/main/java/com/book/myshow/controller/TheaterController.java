package com.book.myshow.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.book.myshow.dto.TheaterShowResponse;
import com.book.myshow.service.ShowService;

@RestController
@RequestMapping(("/api/v1/search"))
public class TheaterController {

    private final ShowService showService;
    
    public TheaterController(ShowService showService) {
        this.showService = showService;
    }
    
    @GetMapping("/theaters")
    public ResponseEntity<List<TheaterShowResponse>> getTheatersByMovieAndDate(
            @RequestParam Long movieId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<TheaterShowResponse> theaters = showService.getTheatersByMovieAndDate(movieId, date);
        return ResponseEntity.ok(theaters);
    }


}
