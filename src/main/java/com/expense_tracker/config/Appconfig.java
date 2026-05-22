package com.expense_tracker.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.expense_tracker.security.jwt.CachedBodyFilter;

@Configuration
public class Appconfig {
    
	@Bean
	public BCryptPasswordEncoder getBCryptPasswordEncoder() {

		return new BCryptPasswordEncoder(12);
	}

//	@Bean
//	public ObjectMapper objectMapper() {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.registerModule(new JavaTimeModule());
//		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//		 mapper.registerModule(new ParameterNamesModule());
//		    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		return mapper;
//	}
	
	@Bean
	public FilterRegistrationBean<CachedBodyFilter> cachedBodyFilter() {
	    FilterRegistrationBean<CachedBodyFilter> registration = 
	        new FilterRegistrationBean<>();
	    registration.setFilter(new CachedBodyFilter());
	    registration.addUrlPatterns("/*");
	    registration.setOrder(-100);
	    return registration;
	}
	
   
}
