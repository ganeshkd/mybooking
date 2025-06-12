package com.book.myshow.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "show")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Show {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "show_id")
	private Long showId;

    @Column(name = "show_date", nullable = false)
    private LocalDate showDate;
    
    @Column(name = "show_time", nullable = false)
    private LocalTime showTime;
    
    @Column(name = "ticket_price", precision = 8, scale = 2)
    private BigDecimal ticketPrice;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    //many-to-one with Theater (master)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;
    
    //many-to-one with Movie (master)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
    
    //one-to-many with ShowSeat
    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ShowSeat> showSeats;
    
    //one-to-many with Booking
    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;
    
    public Show(LocalDate showDate, LocalTime showTime, Theater theater, Movie movie, BigDecimal ticketPrice) {
        this.showDate = showDate;
        this.showTime = showTime;
        this.theater = theater;
        this.movie = movie;
        this.ticketPrice = ticketPrice;
    }
}