package com.book.myshow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.book.myshow.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
	
	@Query("SELECT m FROM Movie m WHERE m.movieId = :movieId AND m.isActive = true")
	Optional<Movie> findByMovieIdAndIsActiveTrue(Long movieId);

}
