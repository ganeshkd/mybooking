package com.book.myshow.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "theater")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Theater {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "theater_id")
	private Long theaterId;
	
	@Column( name = "theater_name", unique = true, nullable = false, length = 100)
	private String theaterName;

	@Column( name = "location_url", length = 255)
	private String locationUrl;
	
	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "is_active")
	private Boolean isActive;
	
	//one-to-many with seat
	@OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Seat> seats;
	
	//one-to-many with show
	@OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Show> shows;

}
