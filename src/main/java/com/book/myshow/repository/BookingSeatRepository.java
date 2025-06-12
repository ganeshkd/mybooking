package com.book.myshow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.book.myshow.entity.BookingSeat;
import com.book.myshow.entity.ShowSeat;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long>{

    @Query("SELECT bs.showSeat FROM BookingSeat bs WHERE bs.booking.bookingId = :bookingId")
    List<ShowSeat> findShowSeatsByBookingId(@Param("bookingId") Long bookingId);
    
    @Query("SELECT bs.showSeat FROM BookingSeat bs WHERE bs.booking.bookingId = :bookingId")
    List<BookingSeat> findByBookingId(Long bookingId);
}
