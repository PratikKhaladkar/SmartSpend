package com.expense_tracker.security.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.expense_tracker.entities.User;

@Component
public class SecurityUtil {
    
	

	public static User getAuthenticatedUser() {
		
	AppUserDetails appUserDetails= (AppUserDetails)	SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	
	  
	 return appUserDetails.getUser();
	}
}
