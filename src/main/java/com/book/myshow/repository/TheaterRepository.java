package com.book.myshow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.book.myshow.entity.Theater;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
	
    @Query("SELECT t  FROM Theater t WHERE t.theaterId = :theaterId "
    		+ "AND t.isActive = true")
	Optional<Theater> findByTheaterIdAndIsActiveTrue(Long theaterId);

}
