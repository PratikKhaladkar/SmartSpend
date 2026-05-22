package com.expense_tracker.dtos;

import java.time.Instant;

public class ApiErrorResponse {

	private int status;
	private String message;
	private Instant timestamp;

	public ApiErrorResponse(int status, String message) {
		super();
		this.status = status;
		this.message = message;
		this.timestamp = Instant.now();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
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
