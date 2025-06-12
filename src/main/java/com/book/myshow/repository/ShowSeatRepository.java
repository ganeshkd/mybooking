package com.book.myshow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.book.myshow.entity.ShowSeat;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long>{

    @Query("SELECT ss FROM ShowSeat ss JOIN ss.seat s WHERE ss.show.showId = :showId " +
            "AND s.seatName = :seatName")
     Optional<ShowSeat> findByShowAndSeatName(
         @Param("showId") Long showId,
         @Param("seatName") String seatName
     );
     
     @Query("SELECT ss FROM ShowSeat ss WHERE ss.show.showId = :showId AND ss.isAvailable = true")
     List<ShowSeat> findAvailableSeatsByShow(@Param("showId") Long showId);
}
