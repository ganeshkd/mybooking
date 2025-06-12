package com.book.myshow.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.book.myshow.dto.ShowRequest;
import com.book.myshow.dto.ShowResponse;
import com.book.myshow.dto.TheaterShowResponse;
import com.book.myshow.entity.Movie;
import com.book.myshow.entity.Show;
import com.book.myshow.entity.Theater;
import com.book.myshow.repository.MovieRepository;
import com.book.myshow.repository.ShowRepository;
import com.book.myshow.repository.TheaterRepository;

import jakarta.persistence.EntityNotFoundException;


@Service
@Transactional
public class ShowService {
    private final ShowRepository showRepository;
    private final TheaterRepository theaterRepository;
    private final MovieRepository movieRepository;
    
    public ShowService(ShowRepository showRepository, 
                      TheaterRepository theaterRepository,
                      MovieRepository movieRepository) {
        this.showRepository = showRepository;
        this.theaterRepository = theaterRepository;
        this.movieRepository = movieRepository;
    }
    
    public ShowResponse createShow(ShowRequest request) {
        // Validate theater exists and is active
        Theater theater = theaterRepository.findByTheaterIdAndIsActiveTrue(request.getTheaterId())
            .orElseThrow(() -> new EntityNotFoundException("Theater not found or inactive"));
        
        // Validate movie exists and is active
        Movie movie = movieRepository.findByMovieIdAndIsActiveTrue(request.getMovieId())
            .orElseThrow(() -> new EntityNotFoundException("Movie not found or inactive"));
        
        // Check for conflicting shows
        if (showRepository.existsByTheaterIdAndShowDateAndShowTime(
                request.getTheaterId(), request.getShowDate(), request.getShowTime())) {
            throw new IllegalStateException("Show already exists for this theater at the same date and time");
        }
        
        Show show = new Show(request.getShowDate(), request.getShowTime(), 
                           theater, movie, request.getTicketPrice());
        
        Show savedShow = showRepository.save(show);
        return new ShowResponse(savedShow);
    }
    
    public ShowResponse updateShow(Long showId, ShowRequest request) {
        Show existingShow = showRepository.findById(showId)
            .orElseThrow(() -> new EntityNotFoundException("Show not found"));
        
        // Validate theater exists and is active
        Theater theater = theaterRepository.findByTheaterIdAndIsActiveTrue(request.getTheaterId())
            .orElseThrow(() -> new EntityNotFoundException("Theater not found or inactive"));
        
        // Validate movie exists and is active
        Movie movie = movieRepository.findByMovieIdAndIsActiveTrue(request.getMovieId())
            .orElseThrow(() -> new EntityNotFoundException("Movie not found or inactive"));
        
        // Check for conflicting shows (excluding current show)
        if (!existingShow.getTheater().getTheaterId().equals(request.getTheaterId()) ||
            !existingShow.getShowDate().equals(request.getShowDate()) ||
            !existingShow.getShowTime().equals(request.getShowTime())) {
            
            if (showRepository.existsByTheaterIdAndShowDateAndShowTime(
                    request.getTheaterId(), request.getShowDate(), request.getShowTime())) {
                throw new IllegalStateException("Show already exists for this theater at the same date and time");
            }
        }
        
        existingShow.setShowDate(request.getShowDate());
        existingShow.setShowTime(request.getShowTime());
        existingShow.setTheater(theater);
        existingShow.setMovie(movie);
        existingShow.setTicketPrice(request.getTicketPrice());
        
        Show updatedShow = showRepository.save(existingShow);
        return new ShowResponse(updatedShow);
    }
    
    public void deleteShow(Long showId) {
        if (!showRepository.existsById(showId)) {
            throw new EntityNotFoundException("Show not found");
        }
        showRepository.deleteById(showId);
    }
    
    @Transactional(readOnly = true)
    public List<ShowResponse> getShowsByTheaterAndDate(Long theaterId, LocalDate showDate) {
        List<Show> shows = showRepository.findByTheaterIdAndShowDate(theaterId, showDate);
        return shows.stream()
                   .map(ShowResponse::new)
                   .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<TheaterShowResponse> getTheatersByMovieAndDate(Long movieId, LocalDate showDate) {
        List<Theater> theaters = showRepository.findTheatersByMovieIdAndShowDate(movieId, showDate);
        
        return theaters.stream()
                      .map(theater -> {
                          List<Show> shows = showRepository.findByTheaterIdAndShowDate(
                              theater.getTheaterId(), showDate)
                              .stream()
                              .filter(show -> show.getMovie().getMovieId().equals(movieId))
                              .collect(Collectors.toList());
                          
                          List<ShowResponse> showDtos = shows.stream()
                                                               .map(ShowResponse::new)
                                                               .collect(Collectors.toList());
                          
                          return new TheaterShowResponse(theater, showDtos);
                      })
                      .collect(Collectors.toList());
    }


}
