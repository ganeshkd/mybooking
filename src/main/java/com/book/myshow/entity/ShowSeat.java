package com.book.myshow.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "show_seat", uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"show_id", "seat_id"})})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ShowSeat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "show_seat_id")
	private Long showSeatId;

    @Column(name = "is_available")
    private Boolean isAvailable = true;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "seat_status", length = 20)
    private SeatStatus seatStatus = SeatStatus.available;
    
    //many-to-one with Show
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;
    
    //many-to-one with Seat
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;
    
    //one-to-many with BookingSeat
    @OneToMany(mappedBy = "showSeat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookingSeat> bookingSeats;

    // Enum for seat status
    public enum SeatStatus {
        available, booked, blocked
    }
}
