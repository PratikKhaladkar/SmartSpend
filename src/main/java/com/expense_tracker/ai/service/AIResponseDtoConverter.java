package com.expense_tracker.ai.service;

import com.expense_tracker.dtos.AIResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AIResponseDtoConverter implements AttributeConverter<AIResponseDto, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(AIResponseDto dto) {
        if (dto == null) return null;
        try {
            return mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing AIResponseDto", e);
        }
    }


	@Override
	public AIResponseDto convertToEntityAttribute(String json) {
		 if (json == null) return null;
	        try {
	            return mapper.readValue(json, AIResponseDto.class);
	        } catch (JsonProcessingException e) {
	            throw new RuntimeException("Error deserializing AIResponseDto", e);
	        }
	}
}
