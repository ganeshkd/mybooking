package com.book.myshow.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.book.myshow.entity.Show;
import com.book.myshow.entity.Theater;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long>{
    @Query("SELECT s FROM Show s JOIN s.theater t WHERE t.theaterName = :theaterName " +
            "AND s.showDate = :showDate AND s.showTime = :showTime")
     Optional<Show> findByTheaterNameAndShowDateAndShowTime(
         @Param("theaterName") String theaterName,
         @Param("showDate") LocalDate showDate,
         @Param("showTime") LocalTime showTime
     );
     
     @Query("SELECT s FROM Show s JOIN s.theater t WHERE t.theaterName = :theaterName " +
            "AND s.showDate = :showDate")
     List<Show> findByTheaterNameAndShowDate(
         @Param("theaterName") String theaterName,
         @Param("showDate") LocalDate showDate
     );
     
     @Query("SELECT EXISTS( SELECT 1 FROM Show s JOIN s.theater t WHERE t.theaterId = :theaterId " +
             "AND s.showDate = :showDate " +
    		 "AND s.showTime = :showTime)")
	boolean existsByTheaterIdAndShowDateAndShowTime(Long theaterId, LocalDate showDate, LocalTime showTime);
    
     @Query("SELECT s FROM Show s JOIN s.theater t WHERE t.theaterId = :theaterId " +
             "AND s.showDate = :showDate ")
	List<Show> findByTheaterIdAndShowDate(Long theaterId, LocalDate showDate);

     @Query("SELECT DISTINCT s.theater FROM Show s JOIN s.movie m WHERE m.movieId = :movieId " +
             "AND s.showDate = :showDate ")
	List<Theater> findTheatersByMovieIdAndShowDate(Long movieId, LocalDate showDate);
}
