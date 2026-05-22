package com.expense_tracker.dtos;

import com.expense_tracker.entities.ChatHistory;

public class ChatHistoryRepsonseDto {

	private String query;

	private AIResponseDto response;

	
	public ChatHistoryRepsonseDto(ChatHistory chatHistory) {
		super();
		this.query=chatHistory.getQuery();
		this.response=chatHistory.getResponse();

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

}
