package com.book.myshow.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.book.myshow.dto.ShowRequest;
import com.book.myshow.dto.ShowResponse;
import com.book.myshow.service.ShowService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/theaters/{theaterId}/shows")
@Validated
public class TheaterShowController {
    private final ShowService showService;
    
    public TheaterShowController(ShowService showService) {
        this.showService = showService;
    }
    
    @PostMapping
    public ResponseEntity<ShowResponse> createShow(
            @PathVariable Long theaterId,
            @Valid @RequestBody ShowRequest request) {
        
        request.setTheaterId(theaterId); // Ensure theater ID matches path
        ShowResponse response = showService.createShow(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{showId}")
    public ResponseEntity<ShowResponse> updateShow(
            @PathVariable Long theaterId,
            @PathVariable Long showId,
            @Valid @RequestBody ShowRequest request) {
        
        request.setTheaterId(theaterId); // Ensure theater ID matches path
        ShowResponse response = showService.updateShow(showId, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{showId}")
    public ResponseEntity<Void> deleteShow(
            @PathVariable Long theaterId,
            @PathVariable Long showId) {
        
        showService.deleteShow(showId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    public ResponseEntity<List<ShowResponse>> getShowsByDate(
            @PathVariable Long theaterId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<ShowResponse> shows = showService.getShowsByTheaterAndDate(theaterId, date);
        return ResponseEntity.ok(shows);
    }
	
}
