package com.book.myshow.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.book.myshow.dto.BookingRequest;
import com.book.myshow.dto.BookingResponse;
import com.book.myshow.entity.Booking;
import com.book.myshow.entity.BookingSeat;
import com.book.myshow.entity.Show;
import com.book.myshow.entity.ShowSeat;
import com.book.myshow.exception.BookingException;
import com.book.myshow.exception.SeatNotAvailableException;
import com.book.myshow.exception.ShowNotFoundException;
import com.book.myshow.repository.BookingRepository;
import com.book.myshow.repository.BookingSeatRepository;
import com.book.myshow.repository.SeatRepository;
import com.book.myshow.repository.ShowRepository;
import com.book.myshow.repository.ShowSeatRepository;


@Service
@Transactional
public class BookingService {
    @Autowired
    private ShowRepository showRepository;
    
    @Autowired
    private ShowSeatRepository showSeatRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private BookingSeatRepository bookingSeatRepository;
    
    @Autowired
    private SeatRepository seatRepository;
	
    public BookingResponse bookTickets(BookingRequest request) {
        // 1. Find the show based on theater, date, and time
        Show show = findShow(request.getTheaterName(), request.getShowDate(), request.getShowTime());
        
        // 2. Validate and get available seats
        List<ShowSeat> availableSeats = validateAndGetSeats(show, request.getPreferredSeats());
        
        // 3. Create booking
        Booking booking = createBooking(show, request, availableSeats);
        
        // 4. Reserve seats
        reserveSeats(booking, availableSeats);
        
        // 5. Build and return response
        return buildBookingResponse(booking, show, availableSeats);
    }
    
    private Show findShow(String theaterName, java.time.LocalDate showDate, java.time.LocalTime showTime) {
        return showRepository.findByTheaterNameAndShowDateAndShowTime(theaterName, showDate, showTime)
            .orElseThrow(() -> new ShowNotFoundException(
                String.format("No show found for theater '%s' on %s at %s", theaterName, showDate, showTime)));
    }
    
    private List<ShowSeat> validateAndGetSeats(Show show, List<String> preferredSeatNames) {
        List<ShowSeat> requestedSeats = new ArrayList<>();
        List<String> unavailableSeats = new ArrayList<>();
        
        for (String seatName : preferredSeatNames) {
            ShowSeat showSeat = showSeatRepository.findByShowAndSeatName(show.getShowId(), seatName)
                .orElseThrow(() -> new SeatNotAvailableException("Seat " + seatName + " does not exist"));
            
            if (!showSeat.getIsAvailable() || showSeat.getSeatStatus() != ShowSeat.SeatStatus.available) {
                unavailableSeats.add(seatName);
            } else {
                requestedSeats.add(showSeat);
            }
        }
        
        if (!unavailableSeats.isEmpty()) {
            throw new SeatNotAvailableException("The following seats are not available: " + 
                String.join(", ", unavailableSeats));
        }
        
        return requestedSeats;
    }
    
    private Booking createBooking(Show show, BookingRequest request, List<ShowSeat> seats) {
        Booking booking = new Booking();
        booking.setShow(show);
        booking.setCustomerName(request.getCustomerName());
        booking.setCustomerEmail(request.getCustomerEmail());
        booking.setCustomerPhone(request.getCustomerPhone());
        booking.setBookingStatus(Booking.BookingStatus.confirmed);
        
        // Calculate total amount & discount also can be calculated
        BigDecimal totalAmount = show.getTicketPrice().multiply(BigDecimal.valueOf(seats.size()));
        booking.setTotalAmount(totalAmount);
        
        return bookingRepository.save(booking);
    }
    
    private void reserveSeats(Booking booking, List<ShowSeat> seats) {
        for (ShowSeat seat : seats) {
            // Update seat status
            seat.setIsAvailable(false);
            seat.setSeatStatus(ShowSeat.SeatStatus.booked);
            showSeatRepository.save(seat);
            
            // Create booking-seat relationship
            BookingSeat bookingSeat = new BookingSeat(booking, seat);
            bookingSeatRepository.save(bookingSeat);
        }
    }
    
    private BookingResponse buildBookingResponse(Booking booking, Show show, List<ShowSeat> seats) {
        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getBookingId());
        response.setBookingStatus(booking.getBookingStatus().toString());
        response.setBookingDate(booking.getBookingDate());
        response.setCustomerName(booking.getCustomerName());
        response.setCustomerEmail(booking.getCustomerEmail());
        response.setCustomerPhone(booking.getCustomerPhone());
        response.setTotalAmount(booking.getTotalAmount());
        response.setMessage("Booking confirmed successfully!");
        
        // Set show details
        BookingResponse.ShowDetails showDetails = new BookingResponse.ShowDetails(
            show.getShowId(),
            show.getMovie().getMovieName(),
            show.getTheater().getTheaterName(),
            show.getShowDate(),
            show.getShowTime(),
            show.getTicketPrice()
        );
        response.setShowDetails(showDetails);
        
        // Set booked seats
        List<BookingResponse.SeatDetails> seatDetails = seats.stream()
            .map(showSeat -> new BookingResponse.SeatDetails(
                showSeat.getSeat().getSeatName()
            ))
            .collect(Collectors.toList());
        response.setBookedSeats(seatDetails);
        
        return response;
    }
    
    public BookingResponse getBookingDetails(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new BookingException("Booking not found with ID: " + bookingId));
        
        List<ShowSeat> bookedSeats = bookingSeatRepository.findShowSeatsByBookingId(bookingId);
        
        return buildBookingResponse(booking, booking.getShow(), bookedSeats);
    }
    
    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new BookingException("Booking not found with ID: " + bookingId));
        
        if (booking.getBookingStatus() == Booking.BookingStatus.cancelled) {
            throw new BookingException("Booking is already cancelled");
        }
        
        // Release seats
        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(bookingId);
        for (BookingSeat bookingSeat : bookingSeats) {
            ShowSeat showSeat = bookingSeat.getShowSeat();
            showSeat.setIsAvailable(true);
            showSeat.setSeatStatus(ShowSeat.SeatStatus.available);
            showSeatRepository.save(showSeat);
        }
        
        // Update booking status
        booking.setBookingStatus(Booking.BookingStatus.cancelled);
        bookingRepository.save(booking);
    }

}
