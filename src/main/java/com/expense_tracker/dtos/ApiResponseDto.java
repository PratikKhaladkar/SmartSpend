package com.expense_tracker.dtos;

import java.time.Instant;

public class ApiResponseDto {
    
	private String status;
	
	private String message;
	
	private  Instant timestamp;

	

	public ApiResponseDto(String status, String message) {
		super();
		this.status = status;
		this.message = message;
		this.timestamp = Instant.now();
	}

	

	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}
	
	
	
	
}
