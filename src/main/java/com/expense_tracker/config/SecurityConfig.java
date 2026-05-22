package com.expense_tracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import com.expense_tracker.security.jwt.JwtAccessDeniedHandler;
import com.expense_tracker.security.jwt.JwtAuthenticationEntryPoint;
import com.expense_tracker.security.jwt.JwtFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig  {
	
	
	private JwtFilter jwtFilter;
	 
	 private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	 
	 private JwtAccessDeniedHandler jwtAccessDeniedHandler;
	 
	 private UserDetailsService userDetailsService;
	 
	
	 @Autowired
	public void setJwtFilter(JwtFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
	}
	 
	 @Autowired
   public void setJwtAuthenticationEntryPoint(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
	}
     @Autowired
	 public void setJwtAccessDeniedHandler(JwtAccessDeniedHandler jwtAccessDeniedHandler) {
		 this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
	 }

	@Autowired
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		

		httpSecurity
		    .csrf(csrf -> csrf.disable()
		    )

		.exceptionHandling(ex -> ex
	            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
	            .accessDeniedHandler(jwtAccessDeniedHandler)
	        )
		     .authorizeHttpRequests(
		    		 request->
		    		 request
		    	     .requestMatchers("/auth/register","/auth/login")
		    		 .permitAll()
		    		 .anyRequest()
		    		 .authenticated());

	
		httpSecurity.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}
	
	
	
	@Bean
	public AuthenticationProvider authenticationProvider() {

	     DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider(userDetailsService);
	     daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));

	     return daoAuthenticationProvider;

	}


	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

		return configuration.getAuthenticationManager();
	}
	
	
	
   
}
