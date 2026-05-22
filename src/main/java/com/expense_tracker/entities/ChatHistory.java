package com.expense_tracker.entities;

import java.time.Instant;
import java.util.UUID;

import com.expense_tracker.ai.service.AIResponseDtoConverter;
import com.expense_tracker.dtos.AIResponseDto;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class ChatHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NotBlank
	private String query;

	@Convert(converter = AIResponseDtoConverter.class)
    @Column(columnDefinition = "MEDIUMTEXT")
	private AIResponseDto response;

	@ManyToOne
	private User user;

	private Instant createdAt;

	
	
	public ChatHistory() {
		super();
		
	}
	

	public ChatHistory(@NotBlank String query, AIResponseDto response, User user) {
		super();
		
		this.query = query;
		this.response = response;
		this.user = user;
		this.createdAt = Instant.now();
	}


	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public AIResponseDto getResponse() {
		return response;
	}

	public void setResponse(AIResponseDto response) {
		this.response = response;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public UUID getId() {
		return id;
	}

}
