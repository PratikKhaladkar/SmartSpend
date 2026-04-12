package com.expense_tracker.entities;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class RefreshToken {
  
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@NotBlank
	@Column(nullable = false)
	private String token;
	
	
	@Column(nullable = false)
    private Instant issuedAt;
	
	
	@Column(nullable = false)
	private Instant expiry;
	
	@Column(nullable = false)
	private boolean isConsumed;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
    
	
	public RefreshToken() {
		super();
		
	}

	public RefreshToken(User user) {
		super();
		this.token =UUID.randomUUID().toString();
		this.issuedAt = Instant.now();
		this.expiry = issuedAt.plus(4,ChronoUnit.MINUTES);
		//this.expiry = issuedAt.plus(7,ChronoUnit.DAYS);
		this.user = user;
	}

	public boolean isConsumed() {
		return isConsumed;
	}

	public void setConsumed(boolean isConsumed) {
		this.isConsumed = isConsumed;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Instant getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(Instant issuedAt) {
		this.issuedAt = issuedAt;
	}

	public Instant getExpiry() {
		return expiry;
	}

	public void setExpiry(Instant expiry) {
		this.expiry = expiry;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	
}
