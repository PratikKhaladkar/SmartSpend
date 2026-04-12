package com.expense_tracker.security.jwt;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;



import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	
private JwtFilterUtility jwtFilterUtility;
	
	@Autowired
	public void setJwtFilterUtility(JwtFilterUtility jwtFilterUtility) {
		this.jwtFilterUtility = jwtFilterUtility;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
	
		  String jwtToken = jwtFilterUtility.extractAcessToken(request);
		
		  
		 
		 if(jwtToken!=null) {
			 
			
			 
		
		if( SecurityContextHolder.getContext().getAuthentication()==null) {

			UserDetails userDetails = jwtFilterUtility.autheticateToken(jwtToken);

		  
					 
			   UsernamePasswordAuthenticationToken authentoken=
					   new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

			   authentoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			   SecurityContextHolder.getContext().setAuthentication(authentoken);
		   }
		}
		
   
	
       
		filterChain.doFilter(request, response);

	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
	    String path = request.getServletPath();
         
	   
	   if(path.startsWith("/auth/login") || path.startsWith("/auth/register")) {
		   
		   return true;
	   }
	   
	   return false;
	}

	
}
