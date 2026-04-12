package com.expense_tracker.security.jwt;

import java.io.IOException;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.expense_tracker.dtos.ApiErrorResponse;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
     
	private  ObjectMapper objectMapper;

	   @Autowired
		public void setObjectMapper(ObjectMapper objectMapper) {
			this.objectMapper = objectMapper;
		}

		@Override
	    public void handle(
	            HttpServletRequest request,
	            HttpServletResponse response,
	            AccessDeniedException accessDeniedException
	    ) throws StreamWriteException, DatabindException, IOException {

	        ApiErrorResponse error = new ApiErrorResponse(
	                HttpServletResponse.SC_FORBIDDEN,
	                accessDeniedException.getMessage()
	                
	        );

	        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	        response.setContentType("application/json");

	        objectMapper.writeValue(response.getOutputStream(), error);
	    }
	
}
