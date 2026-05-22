package com.expense_tracker.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public class QueryDto {

	
	@JsonProperty("query")
	@NotBlank(message = "Query required")
	private String query;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public QueryDto() {
		super();

	}

	public QueryDto(@NotBlank String query) {
		super();
		this.query = query;
	}

	@Override
	public String toString() {
		return "QueryDto [query=" + query + "]";
	}
	
	

}
