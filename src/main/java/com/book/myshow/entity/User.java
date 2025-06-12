package com.book.myshow.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
	@Id
	@Column(name="user_id")
	private Integer userId;
	
	@Column(name= "username")
	private String username;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "is_active")
	private boolean isActive;
}
