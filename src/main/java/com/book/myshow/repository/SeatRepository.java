package com.book.myshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.book.myshow.entity.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long>{

}
