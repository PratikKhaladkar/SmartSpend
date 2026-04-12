package com.expense_tracker.security.jwt;

import java.io.IOException;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.expense_tracker.dtos.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.cors.CorsConfigurationSource;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	
	private ObjectMapper objectMapper;

	

	@Autowired
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
                  
	   
		ApiErrorResponse error = new ApiErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");

		objectMapper.writeValue(response.getOutputStream(), error);
	}

}
